

ElementNode = 1
TextNode = 3

MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

var observer = new MutationObserver(function(mutations, observer) {
    // fired when a mutation occurs
    console.log(mutations, observer);
    // ...
});

function record(){
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

function snapShot(){
  return { head:convertElement(document.head),
           body:convertElement(document.body)}
}

function isRelevantNode(elem){
  return    elem.nodeType == TextNode
         || (elem.nodeType == ElementNode && elem.nodeName != "SCRIPT")
}

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
