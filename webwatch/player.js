/**
*  Contains functionality for replaying what the recorder recorded.
*
*/


/**
* Replays the given history. 
* The snapshot is restored instantly. Further changes are done relatively to the original time.
*/
function replay(history){
  
  //Restore the snapshot.
  document.firstChild.replaceChild(restore(history.start.head), document.head);
  document.firstChild.replaceChild(restore(history.start.body), document.body);

  if (history.actions.length){ 
    window.setTimeout(function(){replayActions(history.actions)},
                      history.start.time.getTime()-history.actions[0].time.getTime()) ;
  }
}


/**
* Replays the given actions with correct relative time to each other.
* */
function replayActions(actions){

  var action = actions.shift();
  replayAction(action);
  
  if (actions.length){ 
    window.setTimeout(function(){replayActions(actions)},
                      action.time.getTime()-actions[0].time.getTime()) ;
  }
}

/*
* Does a single action.
*/
function replayAction(action){

  var target = document;
  for (var i = 0; i<action.target.length;i++){
    target = target.childNodes[action.target[i]];
  }

  console.log("replaying", action, "On", target);
  
  if (action.type == "childList"){

    //Insert nodes
    for (var i = action.inserted.length-1;i>=0;i--){
      var newnode = restore(action.inserted[i]);
      if (target.childNodes.length){
        target.insertBefore(newnode, target.childNodes[action.at]);
      }else{
        target.appendChild(newnode);
      }
    }
    
    //Delete Nodes.
    var toRemove = action.removed;
    while (toRemove > 0){
      target.removeChild(target.childNodes[action.at]);
      toRemove--;
    }  
  }

}

/* Creates a HTML-Element/Node from it's serialized form */
function restore(converted){
  
  var elem;
  if (converted.nodeType == TextNode){
    elem = document.createTextNode(converted.data);
  }else{
    elem = document.createElement(converted.nodeName);

    for (var i=0;i<converted.children.length;i++){
      elem.appendChild(restore(converted.children[i]));    
    }

    if (converted.attributes){
      for (attr in converted.attributes){
        elem.setAttribute(attr, converted.attributes[attr]);
      } 
    }
  }

  return elem;
}
