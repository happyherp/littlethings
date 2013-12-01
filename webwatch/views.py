from pyramid.response import Response
from pyramid.renderers import render_to_response

from models import *

from jsonconversion import *

from sqlalchemy.sql import func

import logging


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
          
  return render_to_response('showreplay.mako', 
                            {"recording":recordingToDict(recording)}, 
                            request=request)  
  
def showSession(request):
  session = request.session.query(Session)\
     .filter(Session.id == request.matchdict["id"]).one()
  
  session_dict = {"id":session.id, 
                  "pages" : map(recordingToDict, session.recordings)}
  
  
  return render_to_response('showsession.mako', 
                            {"session":session_dict}, 
                            request=request)  


def getSessionUpdate(request):
  
  recordingupdates = []
  
  allfocus = []
  
  for recording_request in request.json_body:
    
    recording_id = recording_request["id"]
    count = recording_request["count"]
  
    actions = request.session.query(DOMAction)\
                .filter(DOMAction.record_id == recording_id)\
                .filter(DOMAction.position >= count["domactions"]).all()
                
    mouseactions = request.session.query(MouseAction)\
                .filter(MouseAction.record_id == recording_id)\
                .filter(MouseAction.position >= count["mouseactions"]).all()        
                
    focusactions = request.session.query(FocusAction)\
                .filter(FocusAction.record_id == recording_id)\
                .filter(FocusAction.position >= count["focusactions"]).all()
    allfocus = allfocus + focusactions
    
    scrollactions = request.session.query(ScrollAction)\
                .filter(ScrollAction.record_id == recording_id)\
                .filter(ScrollAction.position >= count["scrollactions"]).all()
    
    resizeactions = request.session.query(ResizeAction)\
                .filter(ResizeAction.record_id == recording_id)\
                .filter(ResizeAction.position >= count["resizeactions"]).all()    
              
    recordingupdates.append({          
        "domactions"   : list(map(domActionToDict, actions)),
        "mouseactions" : list(map(mouseActionToDict, mouseactions)),
        "focusactions" : list(map(focusToDict, focusactions)),
        "scrollactions": list(map(scrollToDict, scrollactions)),
        "resizeactions": list(map(resizeToDict, resizeactions)),
        "id":recording_id})
  
  
  allfocus.sort(key=lambda f :f.time)
  
  #TODO: add data of new recordings to reponse.
  
  response = {"page_updates":recordingupdates}
  
    
  return Response(json.dumps(response))
                                      

def receiveReplay(request):
  
  pagehistory_json = request.json_body["pagehistory"]
  sessionId = pagehistory_json["sessionId"]
  session = request.session.query(Session)\
                .filter(Session.id == sessionId).first()
                  
  if (not session):
    session = Session()
    session.id = sessionId
    request.session.add(session)

  #process the initial snapshot
  record = Pagerecording(starthtml = json.dumps(pagehistory_json["starthtml"]), 
                         time = dateutil.parser.parse(pagehistory_json["time"]),
                         url = pagehistory_json["url"],
                         session = session ,
                         windowWidth = pagehistory_json["windowWidth"],
                         windowHeight = pagehistory_json["windowHeight"]
                         )        
  
  
  addChildrenToRecording(record, 
                         request.json_body["pagehistory"]["modifications"],
                         request.json_body["count"])

  request.session.commit()

  return Response(json.dumps({"newid":record.id}))

def receiveReplayUpdate(request):
  
  logging.getLogger("webwatch.views.receiveReplayUpdate").debug("Start")
  
  record = request.session.query(Pagerecording)\
             .filter(Pagerecording.id == request.json_body["id"]).one()
  
  addChildrenToRecording(record, request.json_body["modifications"], request.json_body["count"])
  
  request.session.commit()
  
  logging.getLogger("webwatch.views.receiveReplayUpdate").debug("End")
  
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
  
  config.add_route('getSessionUpdate', '/getSessionUpdate')
  config.add_view(getSessionUpdate, route_name="getSessionUpdate")      