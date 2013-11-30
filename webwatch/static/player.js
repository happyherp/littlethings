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
    document.replaceChild(this.restore(this.history.starthtml),
        document.childNodes[0]);

    this.addActionsToTimer(this.history.modifications);

    this.timer.run();

  };

  this.addActionsToTimer = function(modifications) {

    var _this = this;
    var offsetDate = function(date) {
      return new Date(date.getTime() + _this.offset);
    };

    for (var i = 0; i < modifications.domactions.length; i++) {
      var callback = saveState(this.replayAction.bind(this), 
                               modifications.domactions[i]);
      this.timer.addEvent(callback,
          offsetDate(modifications.domactions[i].time));
    }

    for (var i = 0; i < modifications.mouseactions.length; i++) {
      var callback = saveState(replayMouseAction, modifications.mouseactions[i]);
      this.timer.addEvent(callback,
          offsetDate(modifications.mouseactions[i].time));
    }
  };

  this.stop = function() {
    this.timer.stop();
  };

  // * Recreates a mouse move */
  function replayMouseAction(mouseaction) {

    if (mouseaction.type == "move") {

      var fakemouse = document.getElementById("fakemouse");
      if (!fakemouse) {
        fakemouse = document.createElement("img");
        fakemouse.id = "fakemouse";
        fakemouse.notrelevant = true;
        fakemouse.setAttribute("src", "/static/mouse.png");
        fakemouse.style.position = "absolute";
        fakemouse.style.zIndex = 100;
        fakemouse.zIndex = 100;
        document.body.appendChild(fakemouse);
      }
      fakemouse.style.left = mouseaction.x + "px";
      fakemouse.style.top = mouseaction.y + "px";

    } else if (mouseaction.type == "click") {

      var clickmarker = document.createElement("div");
      clickmarker.notrelevant = true;

      clickmarker.style.position = "absolute";
      clickmarker.style.left = mouseaction.x + "px";
      clickmarker.style.top = mouseaction.y + "px";
      clickmarker.zIndex = 50;
      clickmarker.style.zIndex = 50;

      clickmarker.appendChild(document.createTextNode("*click*"));
      document.body.appendChild(clickmarker);

      window.setTimeout(function() {
        document.body.removeChild(clickmarker);
      }, 500);

    } else {
      log.error("unknown mouseaction type.");
    }
  }

  /**
   * Does a single action.
   */
  this.replayAction = function(action) {

    var target = document;
    for (var i = 0; i < action.target.length; i++) {
      target = target.childNodes[action.target[i]];
    }

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
      elem = document.createTextNode(converted.nodeValue);
    } else if (converted.nodeType == ElementNode) {
      elem = document.createElement(converted.nodeName);

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

}
