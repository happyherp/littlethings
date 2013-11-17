/**
* Module for recontructing the original state of the dom by rewinding changes.
* 
* State of Maps of Node to [Node]. Representing that Nodes children at that time.
* If a node is not present, the state has not changed, and can be taken from the current 
* state of the page. 
*
*/


/**
 * Returns the state the document had before the mutation occurred, based on the next State.
 * 
 */
function rewind(mutation, afterState){
  
  var beforeState = cloneState(afterState);
  
  if (mutation.type == "childList"){
    if (mutation.removedNodes.length != 0 && mutation.addedNodes.length != 0){
      console.error("mutation that does both, insert and remove nodes.");
    }
    
    if (beforeState[mutation.target] === undefined){
      beforeState[mutation.target] = toArray(mutation.target.childNodes);
    }    
    var nodes = beforeState[mutation.target];
    
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


/**
 * Copies a State. Nodes are not copied, only the map and the arrays.
 * 
 * @param state
 */
function cloneState(state){
  
  var newState = {};
  for (var attr in state) {
      if (state.hasOwnProperty(attr)) newState[attr] = state[attr].slice();
  }
  return newState; 
}