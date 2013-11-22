import json
import dateutil.parser
from models import *

def recordingToDict(recording):
  recording_dict = {"start": {"time": recording.time.isoformat(), 
                              "html": json.loads(recording.html),
                              "url" : recording.url},
                    "id": recording.id,
                    "actions"      : list(map(domActionToDict, recording.dom_actions)),
                    "mouseactions" : list(map(mouseActionToDict, recording.mouse_actions)),
                    "focus": list(map(focusToDict, recording.focus_actions))
                    } 
    
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
  
def mouseActionToDict(mouseaction):   
  mouseaction_dict = {"time"     :mouseaction.time.isoformat()}
  objToDict(mouseaction, mouseaction_dict, ("position", "type", "x", "y"))
  return mouseaction_dict
       
def addChildrenToRecording(record, json_obj):
  
  actions = json_obj["actions"]
  count = json_obj["count"]
  
  position = count["dom"]
  for action in actions["actions"]:
    domaction = DOMAction(time=dateutil.parser.parse(action["time"]),
                          position=position, recording=record)
    dictToObj(action, domaction, ("type", "target", "at", "inserted", "removed",
                                  "attributeName", "attributeValue", "nodeValue"))      
    position += 1
    
  position = count["mouse"]
  for mouseaction in actions["mouseactions"]:
    actiontime = dateutil.parser.parse(mouseaction["time"])
    mouseaction_obj = MouseAction(recording = record, position=position,time = actiontime)
    dictToObj(mouseaction, mouseaction_obj, ("type", "x", "y"))    
    position += 1
    
  #add focus 
  position = count["focus"]
  for focus in actions["focus"]:
    FocusAction(recording = record, position = position,
                time = dateutil.parser.parse(focus["time"]))
    position += 1                     

def dictToObj(jsonobj, obj, fields):
  for field in fields:
    if isinstance(jsonobj[field], dict) or isinstance(jsonobj[field], list):
      obj.__setattr__(field, json.dumps(jsonobj[field]))
    else:
      obj.__setattr__(field, jsonobj[field])

def objToDict(obj, dictobj, fields):
  for field in fields:
    dictobj[field] = obj.__getattribute__(field)