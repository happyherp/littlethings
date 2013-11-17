/**
* Module for recontructing the original state of the dom by rewinding changes.
* 
* State of Maps of Node to [Node]. Representing that Nodes children at that time.
* If a node is not present, the state has not changed, and can be taken from the current 
* state of the page. 
*
*/


function State(){
  
  this.parentToChildren = {};
  
  this.getChildren = function(parent){
    
    var children;
    if (this.parentToChildren[parent]){
      children = this.parentToChildren[parent];
    }else{
      children = toArray(parent.childNodes);
    }
    
    
    for (var i = 0; i < children.length; i++){
      if (children[i].parentNode != parent){
        console.error("Returned child with diffrent parent");
      }
    }
    return children;
  };
  
  /**
   * Copies a State. 
   * 
   * @param state
   */  
  this.clone = function(){
    
    var newState = new State();
    for (var attr in this.parentToChildren) {
        if (this.parentToChildren.hasOwnProperty(attr)) 
          newState.parentToChildren[attr] = this.parentToChildren[attr].slice();
    }
    return newState; 
  };    
    

  /**
   * Returns the state the document had before the mutation occurred, based on this state.
   * 
   */
  this.rewind = function (mutation){
    
    var beforeState = this.clone();
    
    if (mutation.type == "childList"){
      if (mutation.removedNodes.length != 0 && mutation.addedNodes.length != 0){
        console.log("mutation that does both, insert and remove nodes.");
      }
      
      if (beforeState.parentToChildren[mutation.target] === undefined){
        beforeState.parentToChildren[mutation.target] = toArray(mutation.target.childNodes);
      }    
      var nodes = beforeState.parentToChildren[mutation.target];
      
      //Delete all inserted Nodes from beforeState
      for (var i = 0; i < mutation.addedNodes.length; i++){
        var added = mutation.addedNodes[i];
        if (nodes.indexOf(added) == -1){
          console.error("node to remove was not found.");
        }
        nodes.splice(nodes.indexOf(added),1);
      }
      
      //Reinsert deleted nodes
      for (var i = 0; i < mutation.removedNodes.length; i++){
        var deleted = mutation.removedNodes[i];
        if (mutation.nextSibling){
          if (nodes.indexOf(mutation.nextSibling) == -1){
            console.error("Node to insert before was not found.");
          } 
          nodes.splice(nodes.indexOf(mutation.nextSibling),0, deleted);
        }else{
          nodes.push(deleted);
        }     
      }
    }
    
    return beforeState;
  }  
  
}



