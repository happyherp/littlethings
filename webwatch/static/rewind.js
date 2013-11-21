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
    //Make a copy of each array, so modifications of the array dont get through to the
    //copy
    for (var i = 0;i<newState.parentToChildren.values.length;i++){
      newState.parentToChildren.values[i] = newState.parentToChildren.values[i].slice();
    }
    
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

/**
 * Finds a path from the root of the document to the given node, by giving all 
 * indexes that lead from the root to that node.
 */
 function findPath(node, state){
   
   if (node == document){
     return [];
   }else if (state.getParent(node)){
     var path = findPath(state.getParent(node), state);
     path.push(findPosition(node, state));
     return path;
   }else{
     console.error("Could not trace element to document.");
     return [];
   }
 }

 /**
 * Returns an index that represents the position of the given node, inside the Parent.
 */
 function findPosition(node, state){
   var s = 0;//Nodes skipped, because of irrelevant type.
   var i = 0; //Count relevant nodes
   var parentChildNodes = state.getChildren(state.getParent(node)); 
   while (i+s<parentChildNodes.length){
     if (parentChildNodes[i+s] == node){
       return i;
     }else if (isRelevantNode(parentChildNodes[i+s])) {i++;} else {s++;}
     
   }
 }



