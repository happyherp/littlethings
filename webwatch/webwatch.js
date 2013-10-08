


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
  converted.nodeName = elem.nodeName
  converted.data = elem.data
  converted.nodeValue = elem.nodeValue

  converted.children = [];
  for (var i = 0;i<elem.childNodes.length;i++){
    converted.children.push(convertElement(elem.childNodes[i]));
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

  var htmlElement = convertElement(document.firstChild);
  return htmlElement;
}
