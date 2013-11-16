/**
*  Contains functionality for replaying what the recorder recorded.
*
*/


/**
* Replays the given history. 
* The snapshot is restored instantly. Further changes are done relatively to the original time.
*/
function replay(history){

  console.log("replaying history", history);
  
  //Restore the snapshot.
  document.replaceChild(restore(history.start.html), document.firstChild);
  
  var offsetDate = function(date){
    var offset =  new Date().getTime() - history.start.time.getTime();
    return new Date(date.getTime()+offset);
  };
  
  
  //TODO: put this somewhere else, so it can be reused for additional events.
  var timer = new Timer();
  
  for (var i=0; i < history.actions.length; i++){
    
    var callback = function (action){
      return  function(){replayAction( action);};
    }(history.actions[i]);
    
    timer.addEvent(callback, offsetDate(history.actions[i].time));
  }
  
  
  for (var i=0; i < history.mousemoves.length; i++){
    
    var callback = function (mousemoves){
      return  function(){replayMouseMove( mousemoves);};
    }(history.mousemoves[i]);
    
    timer.addEvent(callback, offsetDate(history.mousemoves[i].time));
  }  
 
  timer.run();

}

/**
* Needed because a the dates are made to strings when we convert the object to JSON.
*/
function fixTimes(array){
  for (var i=0;i<array.length;i++){
    array[i].time = new Date(array[i].time);
  }
}


//* Recreates a mouse move */
function replayMouseMove(move){

  var fakemouse = document.getElementById("fakemouse");
  if (!fakemouse){
    fakemouse = document.createElement("img");
    fakemouse.id = "fakemouse";
    fakemouse.setAttribute("src", "mouse.png");
    fakemouse.style.position="absolute";
    document.body.appendChild(fakemouse);
  }
  fakemouse.style.left=move.x+"px";
  fakemouse.style.top=move.y+"px";
  
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

    //Insert nodes at end
    if (target.childNodes.length == (action.at+1)){    
      for (var i = 0;i<action.inserted.length;i++){
         var newnode = restore(action.inserted[i]);
         console.log("inserting", newnode, newnode.outerHTML);
         target.appendChild(newnode);
      }
    }else{
      for (var i = action.inserted.length-1;i>=0;i--){
        var newnode = restore(action.inserted[i]);
        console.log("inserting", newnode, newnode.outerHTML);
        target.insertBefore(newnode, target.childNodes[action.at]);
      }
    }
    
    //Delete Nodes.
    var toRemove = action.removed;
    while (toRemove > 0){
      target.removeChild(target.childNodes[action.at]);
      toRemove--;
    }  
  }else if (action.type == "attributes"){
    target.setAttribute(action.attributeName, action.attributeValue);
  }else if (action.type == "characterData"){
    target.nodeValue = action.nodeValue;
  }else{
    throw "unhandled type of action"
  }

}

/* Creates a HTML-Element/Node from it's serialized form */
function restore(converted){
  
  var elem;
  if (converted.nodeType == TextNode){
    elem = document.createTextNode(converted.nodeValue);
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
