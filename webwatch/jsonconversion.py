import json
import dateutil.parser
from models import *

def recordingToDict(recording):
  recording_dict = {"start": {"time": recording.time.isoformat(), 
                              "html": json.loads(recording.html),
                              "url" : recording.url},
                    "id": recording.id}
  
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
   
   
def addChildrenToRecording(record, json_obj):
  position = len(record.dom_actions)
  for action in json_obj["actions"]:
    domaction = DOMAction(time=dateutil.parser.parse(action["time"]),
                          position=position, recording=record)
    dictToObj(action, domaction, ("type", "target", "at", "inserted", "removed",
                                  "attributeName", "attributeValue", "nodeValue"))      
    position += 1
    

  position = len(record.mouse_actions)
  for mouseaction in json_obj["mouseactions"]:
    actiontime = dateutil.parser.parse(mouseaction["time"])
    mouseaction_obj = MouseAction(recording = record, position=position,time = actiontime)
    dictToObj(mouseaction, mouseaction_obj, ("type", "x", "y"))    
    position += 1
    
  #add focus 
  position = len(record.focus_actions)
  for focus in json_obj["focus"]:
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