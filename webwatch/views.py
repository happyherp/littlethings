from pyramid.response import Response
from pyramid.renderers import render_to_response

import dateutil.parser

from models import Session, Pagerecording, DOMAction, MouseAction, FocusAction

from jsonconversion import *

from sqlalchemy.sql import func


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
  session = request.session.query(Session)\
     .filter(Session.id == request.matchdict["id"]).one()
  
  session_dict = {"id":session.id, 
                  "recordings" : map(recordingToDict, session.recordings)}
  
  focuschanges = request.session.query(FocusAction).join(Pagerecording)\
                   .filter(Pagerecording.session_id == session.id)\
                   .order_by(FocusAction.time)
                   
  def focusToDict(focus):
    return {"time":focus.time.isoformat(),
            "record_id":focus.record_id}
                   
  focuschangesArr = list( map(focusToDict,focuschanges))
  
  return render_to_response('showsession.mako', 
                            {"session":session_dict, "focus":focuschangesArr}, 
                            request=request)  


def receiveReplay(request):
  
  sessionId = request.json_body["actions"]["sessionId"]
  session = request.session.query(Session)\
                .filter(Session.id == sessionId).first()
                  
  if (not session):
    session = Session()
    session.id = sessionId
    request.session.add(session)

  #process the initial snapshot
  start = request.json_body["actions"]["start"]

  record = Pagerecording(html = json.dumps(start["html"]), 
                         time = dateutil.parser.parse(start["time"]),
                         url = start["url"],
                         session = session )        
  
  
  addChildrenToRecording(record, request.json_body)

  request.session.commit()

  return Response(json.dumps({"newid":record.id}))

def receiveReplayUpdate(request):
  
  record = request.session.query(Pagerecording)\
             .filter(Pagerecording.id == request.json_body["actions"]["id"]).one()
  
  addChildrenToRecording(record, request.json_body)
  
  request.session.commit()
  
  return Response("OK")
  


def addToConfig(config):
  config.add_route('start', '/')
  config.add_view(showMainPage, route_name='start')

  config.add_route('receiveReplay', '/receiveReplay')
  config.add_view(receiveReplay, route_name="receiveReplay")

  config.add_route('receiveReplayUpdate', '/receiveReplayUpdate')
  config.add_view(receiveReplayUpdate, route_name="receiveReplayUpdate")

  config.add_route('listReplays', '/listReplays')
  config.add_view(listReplays, route_name="listReplays")

  config.add_route('showReplay', '/showReplay/{id}/')
  config.add_view(showReplay, route_name="showReplay")
  
  config.add_route('listSessions', '/listSessions')
  config.add_view(listSessions, route_name="listSessions")
  
  config.add_route('showSession', '/showSession/{id}/')
  config.add_view(showSession, route_name="showSession")    