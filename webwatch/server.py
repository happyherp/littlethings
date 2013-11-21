from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response
from pyramid.renderers import render_to_response

import dateutil.parser

import json
from db import Session as DBSession
from models import Session, Pagerecording, DOMAction, MouseAction, FocusAction

from sqlalchemy.sql import func

import os


def recordingToDict(recording):
  recording_dict = {"start": {"time": recording.time.isoformat(), 
                              "html": json.loads(recording.html),
                              "url" : recording.url}}
  
  recording_dict["actions"] = []
  for action in recording.dom_actions:
    action_dict = {"time"     :action.time.isoformat(),
                   "inserted" :json.loads(action.inserted),
                   "target"   :json.loads(action.target),
                   }
    objToDict(action, action_dict, ("position", "at", "attributeName", 
                                   "attributeValue", "nodeValue", "removed", "type"))
    recording_dict["actions"].append(action_dict)
    
  recording_dict["mouseactions"] = []
  for mouseaction in recording.mouse_actions:
    mouseaction_dict = {"time"     :action.time.isoformat()}
    objToDict(mouseaction, mouseaction_dict, ("position", "type", "x", "y"))
    recording_dict["mouseactions"].append(mouseaction_dict)
    
  recording_dict["focus_actions"] = []
  for focus_action in recording.focus_actions:
    focus_action_dict = {"time": focus_action.time.isoformat(),
                         "position": focus_action.position}
    recording_dict["focus_actions"].append(focus_action_dict)
    
  return recording_dict
            

def dictToObj(jsonobj, obj, fields):
  for field in fields:
    if isinstance(jsonobj[field], dict) or isinstance(jsonobj[field], list):
      obj.__setattr__(field, json.dumps(jsonobj[field]))
    else:
      obj.__setattr__(field, jsonobj[field])

def objToDict(obj, dictobj, fields):
  for field in fields:
    dictobj[field] = obj.__getattribute__(field)

def showMainPage(request, message=""):
  response = render_to_response('main.mako', {'message':message}, request=request)
  return response

def listReplays(request):
  
  
  replays = request.session.query(Pagerecording)
  
  return render_to_response('replaylist.mako', {'replays':replays}, request=request)

def listSessions(request):
  
  ssnTmCnt = request.session.query(Session)\
                   .add_column(func.min(Pagerecording.time))\
                   .add_column(func.count(Pagerecording.id))\
                   .join(Pagerecording).group_by(Session)\
                   .order_by(func.min(Pagerecording.time).desc()).all()


  return render_to_response('sessionlist.mako', {'ssnTmCnt':ssnTmCnt}, request=request)  

def showReplay(request):
  
  
  recording = request.session.query(Pagerecording)\
      .filter(Pagerecording.id == request.matchdict["id"]).one()
      
  recording_json = json.dumps(recordingToDict(recording))
    
  return render_to_response('showreplay.mako', 
                            {"recording_json":recording_json}, 
                            request=request)  
  
def showSession(request):
  session = None
  return render_to_response('showsession.mako', {"session":session}, request=request)  


def receiveReplay(request):
  
  session = request.session.query(Session)\
                .filter(Session.id == request.json_body["sessionId"]).first()
                  
  if (not session):
    session = Session()
    session.id = request.json_body["sessionId"]
    request.session.add(session)

  #process the initial snapshot
  start = request.json_body["start"]

  record = Pagerecording(html = json.dumps(start["html"]), 
                         time = dateutil.parser.parse(start["time"]),
                         url = start["url"],
                         session = session )        
  
  position = 0
  for action in request.json_body["actions"]:
    domaction = DOMAction(time=dateutil.parser.parse(action["time"]),
                          position=position, recording=record)
    dictToObj(action, domaction, ("type", "target", "at", "inserted", "removed",
                                  "attributeName", "attributeValue", "nodeValue"))      
    position += 1
    

  position = 0
  for mouseaction in request.json_body["mouseactions"]:
    actiontime = dateutil.parser.parse(mouseaction["time"])
    mouseaction_obj = MouseAction(recording = record, position=position,time = actiontime)
    dictToObj(mouseaction, mouseaction_obj, ("type", "x", "y"))    
    position += 1
    
  #add focus 
  position = 0
  for focus in request.json_body["focus"]:
    FocusAction(recording = record, position = position,
                time = dateutil.parser.parse(focus["time"]))
    position += 1         

  request.session.commit()

  return Response("OK")

def manageSession(request):
  session = DBSession()  
  def closeSession(request):
    session.close()
  request.add_finished_callback(closeSession)
  return session

if __name__ == '__main__':

  here = os.path.dirname(os.path.abspath(__file__))

  settings = {}
  settings['reload_all'] = True
  settings['debug_all'] = True
  settings['mako.directories'] = os.path.join(here, 'templates')
  

  config = Configurator(settings=settings)
  
  config.add_request_method(manageSession, "session", reify=True)

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
  
  config.add_route('listSessions', '/listSessions')
  config.add_view(listSessions, route_name="listSessions")
  
  config.add_route('showSession', '/showSession/{id}/')
  config.add_view(showSession, route_name="showSession")    

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  server.serve_forever()
