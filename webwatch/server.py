from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response
from pyramid.renderers import render_to_response

import dateutil.parser

import sqlite3
import json

import os

def getCon():
  return sqlite3.connect('env/db')

def rowsToDicts(rows, names):
  '''Takes a list of rows(as returned by an sql-statement) and converts them 
  into dictionaries, using names as keys. Such that the first element of every row
  is matched with the first name and so on.'''

  return map (lambda r: dict(zip(names, r)), rows)

def showMainPage(request, message=""):
  response = render_to_response('main.mako', {'message':message}, request=request)
  return response;

def listReplays(request):

  conn = getCon()
  c = conn.cursor()
  c.execute("SELECT id, time, url FROM userrecording ")
  replays = rowsToDicts(c.fetchall(),["id", "time", "url"])
  conn.close()
  return render_to_response('replaylist.mako', {'replays':replays}, request=request)


def showReplay(request):
 
  conn = getCon()
  c = conn.cursor()
  c.execute("SELECT id, time, url, htmlcontent FROM userrecording where id=? ", 
            (request.matchdict["id"],))
  replay = dict( zip(["id", "time", "url", "htmlcontent" ],c.fetchone()))
  replay["time"] = dateutil.parser.parse(replay["time"])
  
  #add actions
  replay["actions"] = []
  rows = c.execute('''SELECT 
                 time, type, target, at, removed, 
                 attributeName, attributeValue, inserted, nodeValue
               FROM action where fkrecordid = ? order by position ASC''',(replay["id"],))
  for row in rows:
    action = dict( zip(["time", "type", "target", "at", "removed", 
                        "attributeName", "attriuteValue", "inserted", "nodeValue" 
                       ],row))
    action["time"] = dateutil.parser.parse(action["time"]).isoformat()
    action["target"] = json.loads(action["target"])
    action["inserted"] = json.loads(action["inserted"])
    replay["actions"].append(action)
    

  #add mouseactions.
  replay["mouseactions"] = []
  rows = c.execute('''SELECT 
                 time, type, x, y
               FROM mouseaction where fkrecordid = ? order by position ASC''',(replay["id"],))
  for row in rows:
    mouseaction = dict( zip(["time", "type","x", "y" ],row))
    mouseaction["time"] = dateutil.parser.parse(mouseaction["time"]).isoformat()
    replay["mouseactions"].append(mouseaction)
  
  conn.close() 
  return render_to_response('showreplay.mako', {"replay":replay}, request=request)  
  

def receiveReplay(request):

  #process the initial snapshot
  start = request.json_body["start"]

  html = start["html"]
  time = dateutil.parser.parse(start["time"])
  url = start["url"]

  #put replay into db
  conn = getCon()
  c = conn.cursor()
  c.execute("INSERT INTO userrecording (htmlcontent, time, url) VALUES (?,?,?)",
            (json.dumps(html), time, url))
  recordingid = c.lastrowid            
  
  position = 0
  for action in request.json_body["actions"]:
    actiontime = dateutil.parser.parse(action["time"])
    target_json = json.dumps(action["target"])
    inserted_json = json.dumps(action["inserted"])
    c.execute('''INSERT INTO action (fkrecordid, position, time, type, target, at, removed,
                                     attributename, attributevalue, inserted, nodeValue)
                        VALUES(?,?,?,?,?,?,?,?,?,?,?)''', 
               (recordingid, position, actiontime, action["type"], target_json, action["at"],
                action["removed"], action["attributeName"], action["attributeValue"],
                inserted_json, action["nodeValue"]))              
    position += 1
    

  position = 0
  for mouseaction in request.json_body["mouseactions"]:
    actiontime = dateutil.parser.parse(mouseaction["time"])
    c.execute('''INSERT INTO mouseaction (fkrecordid, position, time, type, x, y)
                        VALUES(?,?,?,?,?,?)''', 
               (recordingid, position, actiontime, mouseaction["type"], 
                mouseaction["x"],mouseaction["y"]))              
    position += 1    
        
  conn.commit()

  conn.close()

  return Response("OK")

if __name__ == '__main__':

  here = os.path.dirname(os.path.abspath(__file__))

  settings = {}
  settings['reload_all'] = True
  settings['debug_all'] = True
  settings['mako.directories'] = os.path.join(here, 'templates')
  

  config = Configurator(settings=settings)

  config.include('pyramid_mako')

  config.add_static_view("static", "static", cache_max_age = 0)

  #also show sample page. this is for testing and not part of the project itself. 
  config.add_static_view("samplepage", "samplepage", cache_max_age = 0)


  config.add_route('start', '/')
  config.add_view(showMainPage, route_name='start')

  config.add_route('receiveReplay', '/receiveReplay')
  config.add_view(receiveReplay, route_name="receiveReplay")

  config.add_route('listReplays', '/listReplays')
  config.add_view(listReplays, route_name="listReplays")

  config.add_route('showReplay', '/showReplay/{id}/')
  config.add_view(showReplay, route_name="showReplay")

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  server.serve_forever()
