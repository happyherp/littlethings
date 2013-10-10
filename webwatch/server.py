from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response
from pyramid.renderers import render_to_response, render

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

  conn.commit()

  conn.close()


  #TODO: also process following mutations

  return Response("OK")

if __name__ == '__main__':

  here = os.path.dirname(os.path.abspath(__file__))

  settings = {}
  settings['reload_all'] = True
  settings['debug_all'] = True
  settings['mako.directories'] = os.path.join(here, 'templates')
  

  config = Configurator(settings=settings)

  config.include('pyramid_mako')

  config.add_static_view("static", "static")

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
