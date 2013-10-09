/**
*Contains funtionality for recording the events on a website
*
*/
MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

var observer = new MutationObserver(function(mutations, observer) {
    // fired when a mutation occurs
    //console.log(mutations, observer);

    var serializedNodes = []; //A List of all dom-nodes that have already been
    //serialized on this event. 
    //We need this, because sometimes a node(A) is added to the tree(mutation) and right
    // after that another node(b) is added to A(mutation again). Both mutations are handled in 
    //the same event. o we first see node A. But when we look at it, node b has already been added to it.
    //Then we see node b again on the second mutation. So b will be added twice. This list ought
    // To prevent that.  

    //For each mutation create a serializable action, that can be used to recreate
    // the new state from the previous one.
    for (var i=0;i<mutations.length;i++){

       var mutation = mutations[i];
       var action = mutationToAction(mutation, serializedNodes);
       //console.log("action", action);

       pagehistory.actions.push(action);
    }
});

function mutationToAction(mutation, serializedNodes){
  var action = {time:new Date()};
  action.target = findPath(mutation.target);
  action.type = mutation.type;

  if (mutation.type == "childList"){

   var sibling = mutation.previousSibling;
   while (sibling != null && !isRelevantNode(sibling)){
     sibling = sibling.previousSibling;
   }
   action.at = sibling?findPosition(sibling)+1:0

   action.removed = mutation.removedNodes.length;

   action.inserted = []
   for (var j=0;j<mutation.addedNodes.length;j++){
     var newnode = mutation.addedNodes[j];
     if (isRelevantNode(newnode) && !alreadySerialized(newnode, serializedNodes)){
       action.inserted.push(convertElement(newnode));
       serializedNodes.push(newnode)
     }
   }

  }else if (mutation.type == "attributes"){
   action.attributeName = mutation.attributeName;
   action.attributeValue = mutation.target.getAttribute(
                             mutation.attributeName);
  }else if (mutation.type == "characterData"){
   action.nodeValue = target.data
  }else{
   throw "Unexpected mutation type.";
  }
  return action;
}

/** Checks if the given node, or one of its ancestors in in serialized nodes
*/
function alreadySerialized(node, serializedNodes){
  
  if (node){
    var found = false;
    for (var i=0;i<serializedNodes.length;i++){
      found = found || serializedNodes[i] === node
    }
    return found || alreadySerialized(node.parentNode, serializedNodes);
  }else{
    return false;
  }
  
}

/**
* Finds a path from the root of the document to the given node, by giving all indexes that lead from the root to that node.
*/
function findPath(node){
  if (node.parentNode){
    var path = findPath(node.parentNode)
    path.push(findPosition(node));
    return path;
  }else{
    return [];
  }
}

/**
* Returns an index that represents the position of the given node, inside the Parent.
*/
function findPosition(node){
  var s = 0;//Nodes skipped, because of wrong type.
  var i = 0; //Count relevant nodes
  while (i+s<node.parentNode.childNodes.length){
    if (node.parentNode.childNodes[i+s] == node){
      return i;
    }else if (isRelevantNode(node.parentNode.childNodes[i+s])) {i++} else {s++}
    
  }
}

function record(){

  //Make initial snapshot, then record all actions.
  pagehistory = {start:snapShot(), 
                 actions:[],
                 mousemoves:[] }

  // define what element should be observed by the observer
  // and what types of mutations trigger the callback
  observer.observe(document, {
    subtree: true,
    attributes: true,
    childList: true,
    characterData: true,
    attributeOldValue: true,
    characterDataOldValue: true
  });

  document.body.addEventListener("mousemove",recordMouseMove,false);

  console.log("observer online");
}


function recordMouseMove(event){
  //console.log("mousemove", event);
  pagehistory.mousemoves.push({
    //x:event.clientX,
    //y:event.clientY,
    x:event.pageX,
    y:event.pageY,
    time:new Date()});
}

/*Convert a HTML-Element(or Node) to a serializable JSON-OBject, containing the information we require */
function convertElement(elem){
  var converted = {}
  converted.nodeName = elem.nodeName;
  converted.nodeType = elem.nodeType;
  converted.data = elem.data;
  converted.nodeValue = elem.nodeValue;

  converted.children = [];
  for (var i = 0;i<elem.childNodes.length;i++){
    var childelem = elem.childNodes[i];
    if (isRelevantNode(childelem)){
      converted.children.push(convertElement(childelem));
    }
  }

  if (elem.attributes) {
    converted.attributes = {};
    for(var i=0; i<elem.attributes.length; i++) {
       var attr = elem.attributes[i];
       converted.attributes[attr.name] = attr.value;
    }
  }

  return converted;

}


/*Make a snapshot of the current state of the site */
function snapShot(){
  return { html:convertElement(document.firstChild),
           time:new Date()}
}


