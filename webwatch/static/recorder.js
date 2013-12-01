/**
*Contains funtionality for recording the events on a website
*
*/

function record(){
  var recorder = new Recorder();
  recorder.record();
  pagehistory = recorder.pagehistory;
}


MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

function Recorder(){
  
  
  //All handler-functions must be known, so they can
  //removed when recording stops.
  this.observers = [];
  this.mutation_observer = null;  
  this.mousemove_observer = null;
  this.mouseclick_handler = null;
  this.focus_handler = null;
  this.scroll_observer = null;

  /**
   * Information about DOM-Changes is collected here.
   */
  this.pagehistory = null;
  

  /**
   * Makes a Snapshot of the current State of the DOM and saves
   * all further changes.
   * 
   */
  this.record = function (){
    
    
    //Save State at start of recording.
    this.pagehistory = new Pagehistory();
    this.pagehistory.starthtml = convertElement(document.documentElement, new State());
    this.pagehistory.time = new Date();
    this.pagehistory.url = window.location.href;
    this.pagehistory.windowWidth = window.innerWidth;
    this.pagehistory.windowHeight = window.innerHeight;
    this.pagehistory.sessionId = this.getSessionId();

    this.mutation_observer = new MutationObserver(recordMutation.bind(this));  
  
    // define what element should be observed by the observer
    // and what types of mutations trigger the callback
    this.mutation_observer.observe(document, {
      subtree: true,
      attributes: true,
      childList: true,
      characterData: true,
      attributeOldValue: true,
      characterDataOldValue: true
    });
  
    
    this.mousemove_observer = new IntervalObserver(document.body, "mousemove",false);
    this.mousemove_observer.event.handlers.push(recordMouseMove.bind(this));
    this.mousemove_observer.observe();
          
    this.mouseclick_handler = recordMouseClick.bind(this);
    document.body.addEventListener("mouseup", this.mouseclick_handler, false);
    
    this.focus_handler = recordFocus.bind(this);
    window.addEventListener("focus",this.focus_handler);
    
    this.scroll_observer = new IntervalObserver(window, "scroll", true);
    this.scroll_observer.event.handlers.push(this.__recordScroll.bind(this));
    this.scroll_observer.observe();
    
  
    console.log("observer online");
  };
  
  this.stopRecording = function(){
    this.mutation_observer.disconnect();
    this.mousemove_observer.disconnect();
    document.body.removeEventListener("mouseup", this.mouseclick_handler);
    document.body.removeEventListener("focus", this.focus_handler);
    this.scroll_observer.disconnect();

  };
  
  
  function recordMutation(mutations, observer){

    // fired when a mutation occurs
    console.log(mutations, observer);
    
    
    // In case of multiple mutations we have to keep track 
    // of the state of all childlists before every step, since
    // we only have the state after all of them have occurred.
    var states = [];
    for (var i = mutations.length -1; i>= 0; i--){      
      var afterState;
      if (states.length == 0){
        afterState = new State();
      }else{
        afterState = states[0];
      }
      var prevstate = afterState.rewind(mutations[i]);
      states.splice(0, 0, prevstate);
    }
    //console.log("states", states);

    //For each mutation create a serializable action, that can be used to recreate
    // the new state from the previous one.
    for (var i=0;i<mutations.length;i++){

       var mutation = mutations[i];
       
       //Pick the state that the dom had after this mutation.
       var afterState = i < mutations.length -1 ? states[i+1] : new State();
       
       var action = mutationToAction(mutation, afterState);
       //console.log("action", action);

       this.pagehistory.modifications.domactions.push(action);
    }
  }    
  
  
  function mutationToAction(mutation, afterState){
    var action = {time:new Date()};
    action.target = findPath(mutation.target, afterState);
    action.type = mutation.type;
    
    //Add null as default for unneeded values. makes easier sql later
    action.at = null;
    action.removed = null;
    action.inserted = [];
    action.attributeName = null;
    action.attributeValue = null;
    action.nodeValue = null;  
  
    if (mutation.type == "childList"){
  
     var sibling = mutation.previousSibling;
     while (sibling != null && !isRelevantNode(sibling)){
       sibling = sibling.previousSibling;
     }
     action.at = sibling?findPosition(sibling, afterState)+1:0;
  
     action.removed = mutation.removedNodes.length;
     for (var i = 0; i< mutation.removedNodes.length; i++){
       //console.log("removing", mutation.removedNodes[i]);
       if (!isRelevantNode(mutation.removedNodes[i])){
         console.log("BAD DELETION!", mutation.removedNodes[i]);
       }
     }
  
     action.inserted = [];
     for (var j=0;j<mutation.addedNodes.length;j++){
       var newnode = mutation.addedNodes[j];
       if (isRelevantNode(newnode)){
         //console.log("added", newnode);
         action.inserted.push(convertElement(newnode, afterState));
       }
     }
  
    }else if (mutation.type == "attributes"){
     action.attributeName = mutation.attributeName;
     action.attributeValue = mutation.target.getAttribute(
                               mutation.attributeName);
    }else if (mutation.type == "characterData"){
     action.nodeValue = mutation.target.nodeValue;
    }else{
     throw "Unexpected mutation type.";
    }
   
    return action;
  }
  
  function recordMouseMove(event){
    this.pagehistory.modifications.mouseactions.push({
      type:"move",
      x:event.pageX,
      y:event.pageY,
      time:new Date()});
  }
  
  function recordMouseClick(event){
    this.pagehistory.modifications.mouseactions.push({
      type:"click",
      x:event.pageX,
      y:event.pageY,
      time:new Date()});
  }
  
  function recordFocus(){
    this.pagehistory.modifications.focusactions.push({
      time:new Date()
    });
  }
  
  this.__recordScroll = function(event){
    console.log("adding scroll action");
    
    var left;
    var top;
    if (event.target == document){
      left = window.scrollX;
      top = window.scrollY;
    }else{
      left = event.target.scrollLeft;
      top = event.target.scrollTop;
    }
    
    
    this.pagehistory.modifications.scrollactions.push({
      time:new Date(),
      left:left,
      top:top,
      target:findPath(event.target, new State())
    });
    
  };
  
  
  /*Convert a HTML-Element(or Node) to a serializable JSON-OBject, containing the information we require */
  function convertElement(elem, state){
    var converted = {};
    converted.nodeName = elem.nodeName;
    converted.nodeType = elem.nodeType;
    converted.nodeValue = elem.nodeValue;
  
    converted.children = [];
    var childNodesAtState = state.getChildren(elem);
    for (var i = 0;i < childNodesAtState.length;i++){
      var childelem = childNodesAtState[i];
      if (isRelevantNode(childelem)){
        converted.children.push(convertElement(childelem, state));
      }
    }
  
    if (elem.attributes) {
      converted.attributes = {};
      for(var i=0;i < elem.attributes.length; i++) {
         var attr = elem.attributes[i];
         converted.attributes[attr.name] = attr.value;
      }
    }
    return converted;
  }
  
  this.getSessionId = function(){
    
    var sessionId = getCookie("webwatchsession");
    if (!sessionId){
      sessionId = Math.floor(Math.random() * 10000000000) + "";
      setCookie("webwatchsession", sessionId, 1);
    }
    console.log("sessionId is", sessionId);
    return sessionId;    
  };
  

  /**
   * Finds a path from the root of the document to the given node, by giving all 
   * indexes that lead from the root to that node.
   */
   function findPath(node, state){
     
     if (node == document){
       return [];
     }else if (state.getParent(node)){
       var path = findPath(state.getParent(node), state);
       path.push(findPosition(node, state));
       return path;
     }else{
       console.error("Could not trace element to document.");
       return [];
     }
   }

   /**
   * Returns an index that represents the position of the given node, inside the Parent.
   */
   function findPosition(node, state){
     var s = 0;//Nodes skipped, because of irrelevant type.
     var i = 0; //Count relevant nodes
     var parentChildNodes = state.getChildren(state.getParent(node)); 
     while (i+s<parentChildNodes.length){
       if (parentChildNodes[i+s] == node){
         return i;
       }else if (isRelevantNode(parentChildNodes[i+s])) {i++;} else {s++;}
       
     }
   }  
}



function setupRecorder(){
  recorder = new Recorder();
  recorder.record();
  
  serversender = new Serversender(recorder);
  serversender.startSendLoop();
}


function playrecordLive(){

  //reset cookie   
  setCookie("webwatchsession", "", 1);

  setupRecorder();
  
  //State of cookie is saved in recorder.. reset so nothing more is added
  // to the session.
  setCookie("webwatchsession", "", 1);

  
  //Wait for initial request to be send to server.
  serversender.onReceive.once(function(){
    console.log("initial push to server came back.");
    replaywindow = open("/showSession/"+recorder.pagehistory.sessionId+"/");
  });
      
}
