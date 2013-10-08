/**
*Contains funtionality for recording the events on a website
*
*/
MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

var observer = new MutationObserver(function(mutations, observer) {
    // fired when a mutation occurs
    console.log(mutations, observer);

    //For each mutation create a serializable action, that can be used to recreate
    // the new state from the previous one.
    for (var i=0;i<mutations.length;i++){

       var mutation = mutations[i];
       var action = mutationToAction(mutation);
       console.log("action", action);

       pagehistory.actions.push(action);
    }
});

function mutationToAction(mutation){
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
     action.inserted.push(convertElement(mutation.addedNodes[j]));
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
  pagehistory = {start:snapShot(), actions:[]}

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
  console.log("observer online");
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

/*Checks if a HTML-Node should be transfered to the watcher */
function isRelevantNode(elem){
  return    elem.nodeType == TextNode
         || (elem.nodeType == ElementNode && elem.nodeName != "SCRIPT")
}

