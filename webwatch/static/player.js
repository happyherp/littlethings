/**
 * Contains functionality for replaying what the recorder recorded.
 * 
 */

function Player(history) {

  this.history = history;

  this.offset = null;

  this.timer = new Timer();

  /**
   * Replays the given history. The snapshot is restored instantly. Further
   * changes are done relatively to the original time.
   */
  this.replay = function() {

    if (!this.offset) {
      // Set default so that actions begins immediatly.
      this.offset = new Date().getTime() - this.history.time.getTime();
    }

    console.log("replaying history", this.history);

    // Restore the snapshot.
    this.getContentDocument().replaceChild(this.restore(this.history.starthtml),
        this.getContentDocument().childNodes[0]);
    
    this.resizeContentWindow(this.history),

    this.addActionsToTimer(this.history.modifications);

    this.timer.run();

  };

  this.addActionsToTimer = function(modifications) {

    var _this = this;
    var offsetDate = function(date) {
      return new Date(date.getTime() + _this.offset);
    };
    
    function addToTimer(actions, replayfunction){
      actions.map(function(action){
        var callback = function(){
          replayfunction.bind(_this)(action);
        };
        _this.timer.addEvent(callback, offsetDate(action.time));
      });
    }
    
    addToTimer(modifications.domactions, this.replayAction);
    addToTimer(modifications.mouseactions, this.replayMouseAction);
    addToTimer(modifications.scrollactions, this.replayScrollAction);
    addToTimer(modifications.resizeactions, this.resizeContentWindow);
    
    
  };

  this.stop = function() {
    this.timer.stop();
  };
  
  this.replayScrollAction = function(action){
    var target = this.findTarget(action.target);
    
    if (target == this.getContentDocument()){
      this.getContentWindow().scrollTo(action.left, action.top);
    }else{
      target.scrollTop = action.top;
      target.scrollLeft = action.left;
    }
    
    
  };

  // * Recreates a mouse move */
  this.replayMouseAction = function(mouseaction) {

    if (mouseaction.type == "move") {

      var fakemouse = this.getContentDocument().getElementById("fakemouse");
      if (!fakemouse) {
        fakemouse = this.getContentDocument().createElement("div");
        fakemouse.id = "fakemouse";
        fakemouse.notrelevant = true;
        fakemouse.style.position = "absolute";
        fakemouse.style.zIndex = 100;
        fakemouse.zIndex = 100;
        
        var img = this.getContentDocument().createElement("img");
        img.setAttribute("src", "/static/mouse.png");
        img.style.position = "relative";
        img.style.top = "-5px";//Correct for bad image-margin.        
        img.style.left = "-3px";//Correct for bad image-margin.        
        fakemouse.appendChild(img);
        
        
        
        this.getContentDocument().body.appendChild(fakemouse);
      }
      fakemouse.style.left = mouseaction.x + "px";
      fakemouse.style.top = mouseaction.y + "px" ;

    } else if (mouseaction.type == "click") {

      var clickmarker = this.getContentDocument().createElement("div");
      clickmarker.notrelevant = true;

      clickmarker.style.position = "absolute";
      clickmarker.style.left = mouseaction.x + "px";
      clickmarker.style.top = mouseaction.y + "px";
      clickmarker.zIndex = 50;
      clickmarker.style.zIndex = 50;

      clickmarker.appendChild(this.getContentDocument().createTextNode("*click*"));
      this.getContentDocument().body.appendChild(clickmarker);

      window.setTimeout(function() {
        this.getContentDocument().body.removeChild(clickmarker);
      }.bind(this), 500);

    } else {
      log.error("unknown mouseaction type.");
    }
  };

  /**
   * Does a single action.
   */
  this.replayAction = function(action) {

    var target = this.findTarget(action.target);

    // console.log("replaying", action, "On", target);

    if (action.type == "childList") {

      // Delete Nodes.
      var toRemove = action.removed;
      while (toRemove > 0) {
        var nodeToRemove = target.childNodes[action.at];
        if (!nodeToRemove) {
          console.error("Could not find Node to remove.");
        }
        // console.log("removing", nodeToRemove.outerHTML || nodeToRemove.data);
        target.removeChild(nodeToRemove);
        toRemove--;
      }

      // Inserting Nodes.
      if (target.childNodes.length == action.at) {
        for (var i = 0; i < action.inserted.length; i++) {
          var newnode = this.restore(action.inserted[i]);
          // console.log("inserting", newnode, newnode.outerHTML);
          target.appendChild(newnode);
        }
      } else {
        for (var i = action.inserted.length - 1; i >= 0; i--) {
          var newnode = this.restore(action.inserted[i]);
          // console.log("inserting", newnode, newnode.outerHTML);
          target.insertBefore(newnode, target.childNodes[action.at]);
        }
      }

    } else if (action.type == "attributes") {
      this.__restoreAttribute(target, action.attributeName, action.attributeValue);
    } else if (action.type == "characterData") {
      target.nodeValue = action.nodeValue;
    } else {
      throw "unhandled type of action";
    }

  };

  /** Creates a HTML-Element/Node from it's serialized form */
  this.restore = function(converted) {

    var elem;
    if (converted.nodeType == TextNode) {
      elem =  this.getContentDocument().createTextNode(converted.nodeValue);
    } else if (converted.nodeType == ElementNode) {
      elem = this.getContentDocument().createElement(converted.nodeName);

      for (var i = 0; i < converted.children.length; i++) {
        elem.appendChild(this.restore(converted.children[i]));
      }

      if (converted.attributes) {
        for (var attr in converted.attributes) {
          this.__restoreAttribute(elem, attr, converted.attributes[attr]);
        }
      }

    } else {
      throw "Unknown node type: "+ converted.nodeType;
    }

    return elem;
  };

  this.__restoreAttribute = function(elem, attrname, value) {

      
    if (js_event_names.indexOf(attrname) != -1) {
      // Do not copy javascript-events.
    } else if (attrname == "src" || attrname == "href") {
      // fix relative urls
      var newurl = URI(value).absoluteTo(this.history.url);        
      elem.setAttribute(attrname, newurl.toString());
    }else{
      // Just copy all other elements
      elem.setAttribute(attrname, value);
    }
  

  };
  
  this.findTarget = function(positions){
    var target = this.getContentWindow().document;
    for (var i = 0; i < positions.length; i++) {
      target = relevantChilds(target)[positions[i]];
    }
    return target;
  };
  
  this.getContentDocument = function(){
    return this.getContentWindow().document;
  };
  
  this.getContentWindow = function(){
    return this.getContentIFrame().contentWindow;
  };
  
  this.getContentIFrame = function(){
    return document.getElementById("watchframe");
  };
  
  /**
   * obj must have .windowHeight and .windowWidth.
   * 
   */
  this.resizeContentWindow = function(obj){
    this.getContentIFrame().setAttribute("width", obj.windowWidth);
    this.getContentIFrame().setAttribute("height", obj.windowHeight);
  };

}
