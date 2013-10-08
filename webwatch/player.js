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
  document.replaceChild(restore(history.start.html), document.firstChild);


  if (history.actions.length){ 
    window.setTimeout(function(){replayActions(history.actions)},
                      history.actions[0].time.getTime()-history.start.time.getTime()) ;
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
                      actions[0].time.getTime()-action.time.getTime()) ;
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
      console.log("inserting", newnode, newnode.outerHTML);
      if (target.childNodes.length != (action.at+1)){
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
