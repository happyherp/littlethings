/**
* Module for recontructing the original state of the dom by rewinding changes.
* 
*
*/


function State(){
  
  /**
   * Map parent to its current Children. 
   */
  this.parentToChildren = new Map();;
  
  /**
   * Maps children to its parent.
   * 
   * Parent === null means it has no parent.
   * Parent === undefined means it was not changed.
   */
  this.childrenToParent = new Map();
    
  this.getChildren = function(parent){
    
    var children;
    if (this.parentToChildren.get(parent)){
      children = this.parentToChildren.get(parent);
    }else{
      children = toArray(parent.childNodes);
    }
    
    
    for (var i = 0; i < children.length; i++){
      if (this.getParent(children[i]) != parent){
        console.error("Returned child with diffrent parent");
      }
    }
    return children;
  };
  
  this.getParent = function(child){
    var parent;
    if (this.childrenToParent.get(child) === undefined){
      parent = child.parentNode;
    }else{
      parent = this.childrenToParent.get(child);
    }
    
    return parent;
  };
  
  /**
   * Copies a State. 
   * 
   * @param state
   */  
  this.clone = function(){    
    var newState = new State();    
    newState.parentToChildren = this.parentToChildren.copy();
    newState.childrenToParent = this.childrenToParent.copy();    
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
      
      //Mark node as changed.
      if (beforeState.parentToChildren.get(mutation.target) === undefined){
        beforeState.parentToChildren.set(mutation.target,toArray(mutation.target.childNodes));
      }    
      var nodes = beforeState.parentToChildren.get(mutation.target);
      
      //Delete all inserted Nodes from beforeState
      for (var i = 0; i < mutation.addedNodes.length; i++){
        var added = mutation.addedNodes[i];
        if (nodes.indexOf(added) == -1){
          console.error("node to remove was not found.");
        }
        nodes.splice(nodes.indexOf(added),1);
        if (beforeState.getParent(added) != mutation.target){
          console.error("node was not linked to parent");
        }
        beforeState.childrenToParent.set(added,null);
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
        
        if (beforeState.getParent(deleted) !== null){
          console.error("deleted node still had a parent");
        }
        beforeState.childrenToParent.set(deleted, mutation.target);
        
      }
    }
    
    return beforeState;
  };  
  
}



