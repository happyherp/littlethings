import json
import dateutil.parser
import datetime
from models import *
import logging


def readJSONDate(datestring):
  '''parse Dates in the form of 
  2013-11-30T02:28:25.148Z'''
  
  return datetime.datetime.strptime(datestring, "%Y-%m-%dT%H:%M:%S.%fZ")


logger =  logging.getLogger("webwatch.jsonconversion")


def recordingToDict(recording):
  recording_dict = {"time": recording.time.isoformat(), 
                    "starthtml": json.loads(recording.starthtml),
                    "url" : recording.url,
                    "id": recording.id,
                    "sessionId" : recording.session_id,
                    "modifications":
                      {
                       "domactions"   : list(map(domActionToDict, recording.dom_actions)),
                       "mouseactions" : list(map(mouseActionToDict, recording.mouse_actions)),
                       "focusactions" : list(map(focusToDict, recording.focus_actions)),
                       "scrollactions": list(map(scrollToDict, recording.scroll_actions)),
                       "resizeactions": list(map(resizeToDict, recording.resize_actions))                       
                      }
                    } 
  
  objToDict(recording, recording_dict, ("windowWidth", "windowHeight"))
    
  return recording_dict
   
                   
def focusToDict(focus):
  return {"time":focus.time.isoformat(),
          "record_id":focus.record_id,
          "position": focus.position}   
  
def domActionToDict(action):
    action_dict = {"time"     :action.time.isoformat(),
                   "inserted" :json.loads(action.inserted),
                   "target"   :json.loads(action.target),
                   }
    objToDict(action, action_dict, ("position", "at", "attributeName", 
                                   "attributeValue", "nodeValue", "removed", "type"))
    return action_dict   
  
def scrollToDict(scroll):
  scroll_dict = {"time":scroll.time.isoformat(),
                 "target":json.loads(scroll.target)}
  
  objToDict(scroll, scroll_dict, ("record_id", "position", "left", "top"))
  
  return scroll_dict;

def resizeToDict(resize):
  resize_dict = {"time":resize.time.isoformat()}
  
  objToDict(resize, resize_dict, ("record_id", "position", "windowWidth", "windowHeight"))
  
  return resize_dict;
  
def mouseActionToDict(mouseaction):   
  mouseaction_dict = {"time"     :mouseaction.time.isoformat()}
  objToDict(mouseaction, mouseaction_dict, ("position", "type", "x", "y"))
  return mouseaction_dict
       
def addChildrenToRecording(record, json_modifications, json_count):
  
  logger.log(logging.DEBUG, "addChildrenToRecording -> Start")
  
  position = json_count["domactions"]
  for action in json_modifications["domactions"]:
    domaction = DOMAction(time=readJSONDate(action["time"], ) ,
                          position=position, recording=record)
    dictToObj(action, domaction, ("type", "target", "at", "inserted", "removed",
                                  "attributeName", "attributeValue", "nodeValue"))      
    position += 1
    
  position = json_count["mouseactions"]
  for mouseaction in json_modifications["mouseactions"]:
    actiontime = readJSONDate(mouseaction["time"])
    MouseAction(recording = record, position=position,
                            time = actiontime, type = mouseaction["type"],
                            x = mouseaction["x"], y = mouseaction["y"])
    position += 1
    
  #add focus 
  position = json_count["focusactions"]
  for focus in json_modifications["focusactions"]:
    FocusAction(recording = record, position = position,
                time = dateutil.parser.parse(focus["time"]))
    position += 1       
    
  position = json_count["scrollactions"]
  for scroll_dict in json_modifications["scrollactions"]:
    scrollaction = ScrollAction(
                 recording = record, 
                 position = position,
                 time = dateutil.parser.parse(scroll_dict["time"]))
    dictToObj(scroll_dict, scrollaction, ("left", "top", "target"))
    position += 1        
    
    
  position = json_count["resizeactions"]
  for resize_dict in json_modifications["resizeactions"]:
    resizeaction = ResizeAction(
                 recording = record, 
                 position = position,
                 time = dateutil.parser.parse(resize_dict["time"]))
    dictToObj(resize_dict, resizeaction, ("windowWidth", "windowHeight"))
    position += 1                      
    
  logger.log(logging.DEBUG, "addChildrenToRecording -> End")


def dictToObj(jsonobj, obj, fields):
  for field in fields:
    if isinstance(jsonobj[field], dict) or isinstance(jsonobj[field], list):
      obj.__setattr__(field, json.dumps(jsonobj[field]))
    else:
      obj.__setattr__(field, jsonobj[field])

def objToDict(obj, dictobj, fields):
  for field in fields:
    dictobj[field] = obj.__getattribute__(field)