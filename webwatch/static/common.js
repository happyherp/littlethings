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
      this.handlers[i].apply(arguments);
    }
  };
  
}
