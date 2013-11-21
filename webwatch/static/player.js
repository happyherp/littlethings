/**
*  Contains functionality for replaying what the recorder recorded.
*
*/


function Player(history){
  
  this.history = history;
  
  this.timer = new Timer();
  
  /**
   * Replays the given history. 
   * The snapshot is restored instantly. Further changes are done relatively to the original time.
   */  
  this.replay = function(){

    console.log("replaying history", this.history);
    
    //Restore the snapshot.
    document.replaceChild(restore(this.history.start.html), document.firstChild);
    
    var _this = this;
    var offsetDate = function(date){
      var offset =  new Date().getTime() - _this.history.start.time.getTime();
      return new Date(date.getTime()+offset);
    };
    
        
    for (var i=0; i < this.history.actions.length; i++){
      var callback = saveState(replayAction, this.history.actions[i]);
      this.timer.addEvent(callback, offsetDate(this.history.actions[i].time));
    }
    
    
    for (var i=0; i < this.history.mouseactions.length; i++){
      var callback = saveState(replayMouseAction, this.history.mouseactions[i]);
      this.timer.addEvent(callback, offsetDate(this.history.mouseactions[i].time));
    }  
    
   
    this.timer.run();

  };

  //* Recreates a mouse move */
  function replayMouseAction(mouseaction){
    
    if(mouseaction.type=="move"){

      var fakemouse = document.getElementById("fakemouse");
      if (!fakemouse){
        fakemouse = document.createElement("img");
        fakemouse.id = "fakemouse";
        fakemouse.notrelevant = true;
        fakemouse.setAttribute("src", "/static/mouse.png");
        fakemouse.style.position="absolute";
        fakemouse.style.zIndex = 100;
        fakemouse.zIndex = 100;
        document.body.appendChild(fakemouse);
      }
      fakemouse.style.left=mouseaction.x+"px";
      fakemouse.style.top=mouseaction.y+"px";
      
    }else if (mouseaction.type == "click"){
      
      var clickmarker = document.createElement("div");
      clickmarker.notrelevant = true;
    
      clickmarker.style.position="absolute";
      clickmarker.style.left=mouseaction.x+"px";
      clickmarker.style.top=mouseaction.y+"px";
      clickmarker.zIndex = 50;
      clickmarker.style.zIndex = 50;
          
      clickmarker.appendChild(document.createTextNode("*click*"));    
      document.body.appendChild(clickmarker);
      
      window.setTimeout(function(){
        document.body.removeChild(clickmarker);
      },500);
      
    }else{
      log.error("unknown mouseaction type.");
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
      
      //Delete Nodes.
      var toRemove = action.removed;
      while (toRemove > 0){
        var nodeToRemove = target.childNodes[action.at];
        if (!nodeToRemove){
          console.error("Could not find Node to remove.");
        }
        console.log("removing", nodeToRemove.outerHTML || nodeToRemove.data);
        target.removeChild(nodeToRemove);
        toRemove--;
      }  
          
      //Inserting Nodes.
      if (target.childNodes.length == action.at){    
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
      
    }else if (action.type == "attributes"){
      target.setAttribute(action.attributeName, action.attributeValue);
    }else if (action.type == "characterData"){
      target.nodeValue = action.nodeValue;
    }else{
      throw "unhandled type of action";
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
}

function fixTimesInPagehistory(pagehistory){
  pagehistory.start.time = new Date(pagehistory.start.time);
  fixTimes(pagehistory.actions);
  fixTimes(pagehistory.mouseactions);
}
  
