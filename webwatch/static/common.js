/**
* Code shared among recorder and player
*/

ElementNode = 1;
TextNode = 3;


/*Checks if a HTML-Node should be transfered to the watcher */
function isRelevantNode(elem){
  return    (elem.nodeType == TextNode
                 || elem.nodeType == ElementNode 
                     && elem.nodeName != "SCRIPT" 
                     && !elem.notrelevant);
}

function relevantChilds(node){
  return toArray(node.childNodes).filter(isRelevantNode); 
}


/**
* Debug-Tool for figuring out why two nodes dont have the same checksum.
* Ignores all the stuff that is not relevant.(not transfered to replay)
*/

function compareNodes(nodeA,nodeB){

  console.log("comparing", nodeA, nodeB);

  if (checksumForNode(nodeA) == checksumForNode(nodeB)){
    return true;
  }else{
    var childsA = relevantChilds(nodeA);
    var childsB = relevantChilds(nodeB);
    if (childsA.length != childsB.length){
      console.log(nodeA,nodeB, "unequal number of children");
      console.log("A:", childsA, "B:", childsB);
    }else{
      var allsame = true;
      for (var i = 0; i<childsA.length;i++){
        allsame = allsame && compareNodes(childsA[i], childsB[i]);
      }
      if (allsame){
        console.log("childrens match up. problem must be in nodes", nodeA, nodeB);
      }
    }
    return false;
  }
}


/**
* Calculates an integer that is the checksum for the given node.
*/
function checksumForNode(node){
  var checksum = 0;
  if (isRelevantNode(node)){
    checksum = stringHash(node.nodeName);
    checksum += node.nodeValue? stringHash(node.nodeValue):0;
    var children = toArray(node.childNodes).filter(isRelevantNode);
    for (var i=0;i<children.length;i++){
      checksum += checksumForNode(children[i])*(i+2);
    }
    if (node.attributes){
      for(var i=0; i<node.attributes.length; i++) {
         var attr = node.attributes[i];
         checksum += stringHash(attr.value)+stringHash(attr.name);
      }
    }
  }
  return checksum % (1000*1000);

}

/**
* Calculate hash from string. from http://stackoverflow.com/questions/7616461/generate-a-hash-from-string-in-javascript-jquery
*/
function stringHash(s){
  return s.split("").reduce(function(a,b){a=((a<<5)-a)+b.charCodeAt(0);return a&a;},0);              
}

//http://stackoverflow.com/questions/2735067/how-to-convert-a-dom-node-list-to-an-array-in-javascript
function toArray(obj) {
  var array = [];
  // iterate backwards ensuring that length is an UInt32
  for (var i = obj.length >>> 0; i--;) { 
    array[i] = obj[i];
  }
  return array;
}

function newAjax(){
  var xmlhttp;
  if (window.XMLHttpRequest){
    // code for IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp=new XMLHttpRequest();
  }else{// code for IE6, IE5
    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }
  return xmlhttp;
}

function post(url, value, callback){
  var xmlhttp = newAjax();
  xmlhttp.onreadystatechange=function(){
    if (xmlhttp.readyState==4 && xmlhttp.status==200){
      callback(xmlhttp.responseText);
    }
  };
  xmlhttp.open("POST",url,true);
  xmlhttp.send(value);
}

/**
 * Usage saveState(foo, args..)
 * 
 * Creates a function bound that calls foo with the given args followed by the arguments of bound.
 * 
 */
function saveState(){
  
  var foo = arguments[0];
  var rest = toArray(arguments);
  rest.splice(0,1);
  
  return function(){
    var newargs = rest.concat(toArray(arguments));
    foo.apply(null,newargs);
  };  
}


/**
* Basic Observer Pattern
*/
function Event(){
  
  this.handlers = [];
  
  this.fire = function(){
    for (var i = 0; i< this.handlers.length; i++){
      this.handlers[i].apply(null, arguments);
    }
  };
  
  /**
   * Calls foo the next time the event is triggered, but not again.
   * 
   */
  this.once = function(foo){
    var callAndRemove;
    var _this = this;
    callAndRemove = function(){
      foo.apply(arguments);
      _this.handlers.splice(_this.handlers.indexOf(callAndRemove),1);
    };
    this.handlers.push(callAndRemove);
  };
  
}


function getCookie(c_name) {
  var c_value = document.cookie;
  var c_start = c_value.indexOf(" " + c_name + "=");
  if (c_start == -1) {
    c_start = c_value.indexOf(c_name + "=");
  }
  if (c_start == -1) {
    c_value = null;
  } else {
    c_start = c_value.indexOf("=", c_start) + 1;
    var c_end = c_value.indexOf(";", c_start);
    if (c_end == -1) {
      c_end = c_value.length;
    }
    c_value = unescape(c_value.substring(c_start, c_end));
  }
  return c_value;
}

function setCookie(c_name, value, exdays) {
  var exdate = new Date();
  exdate.setDate(exdate.getDate() + exdays);
  var c_value = escape(value)
      + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
  document.cookie = c_name + "=" + c_value;
}


/**
* Needed because a the dates are made to strings when we convert the object to JSON.
*/
function fixTimes(array){
  for (var i=0;i<array.length;i++){
    array[i].time = new Date(array[i].time);
  }
}

/**
 * 
 * Copies the given attributes from one object to another.
 * 
 * Usage copyAttributes(source, dest, attr...)
 * 
 */
function copyAttributes(){
  var source = arguments[0];
  var dest = arguments[1];
  for (var i = 2; i<arguments.length;i++){
    var attrname = arguments[i];
    dest[attrname] = source[attrname];
  }  
}

function copyAllAttributes(source, dest){
  for (var key in source){
    dest[key] = source[key];
  }
}


/**
 * An Observer for mouse moves, that only gets fired for a mouse-move
 * after a certain time has passes. this is done to imporve
 * performance, as otherwise onMouseMove eats up all the cpu.
 */
function FastMouseMoveObserver(){
  
  this.event = new Event();
  
  this.waittime = 50;
  
  var off = 0;
  var listening = 1;
  var waitingForTimeout = 2;
  
  this.state = off;
    
  this.__movelistener = null;
    
  this.observe = function(){
    
    if (this.state == off){  
      this.state = listening;
      this.__register();
    }
    
  };
  
  this.disconnect = function(){
    if (this.state == listening){
      document.body.removeEventListener("mousemove", this.__movelistener);      
    }
    this.state = off;
  };
  
  this.__register = function(){
      
    //Movelistener is bound to this. and gets called on the bodys 
    //mousemove-event. It the removes itself as a listener and
    //sets a timeout that will reconnect it to the bodys events
    //after a certain time has passed.
    this.__movelistener = this.__onMouseMove.bind(this);      
    
    document.body.addEventListener("mousemove",this.__movelistener ,false);    
  
  };
  
  this.__onMouseMove = function(event){
    
    if (this.state == listening){
      document.body.removeEventListener("mousemove", this.__movelistener);
      this.event.fire(event);   
      this.state = waitingForTimeout;
      window.setTimeout(this.__onTimeout.bind(this), this.waittime);
    
    }else{
      throw "Unexpected state " + this.state;
    }
    
  };
  
  this.__onTimeout = function(){
    if (this.state == waitingForTimeout){
      this.state = listening;
      this.__register();
    }else if (this.state == listening || this.state == off){
      //Do nothing
    }else{
      throw "Unexpected state " + this.state;
    }
  };  
}


js_event_names = ["onabort", "onblur", "onchange","onclick","ondblclick", "onerror",                  
"onfocus", "onkeydown", "onkeypress", "onkeyup", "onload", "onmousedown", "onmousemove",
"onmouseout", "onmouseover", "onmouseup", "onreset", "onselect", "onsubmit", "onunload"]; 
                  
                  


