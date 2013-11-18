/**
 * A Map that can use Node-Objects as keys. 
 * 
 * Linear Search time... :-/
 */


function Map(inkeys, invalues){
  
  var keys = inkeys?inkeys.slice():[];
  var values = invalues?invalues.slice():[];
  
  this.set = function(key, value){
    var index = keys.indexOf(key);
    if (index == -1){
      keys.push(key);
      values.push(value);
    }else{
      values[index] = value;
    }
  };
  
  this.get = function(key){
    var index = keys.indexOf(key);
    if (index == -1){
      return undefined;
    }else{
      return values[index];
    }    
  };
  
  /**
   * Copies the association. keys and values are not deep-copied.
   * 
   */
  this.copy = function(){
    return new Map(keys,values);
  };
}


function testMap(){
  
  var map = new Map();
  var ok = true;
  
  map.set(1,"i");
  
  ok = ok && map.get(1) == "i";
  
  ok = ok && map.get(2) === undefined;
  
  map.set(2,"k");
  
  ok = ok && map.get(1) == "i";
  ok = ok && map.get(2) == "k";
  
  map.set(1,"p");

  ok = ok && map.get(1) == "p";
  ok = ok && map.get(2) == "k";
  
  if (ok){
    console.log("all fine");
  }else{
    console.error("maptest failed");
  }
}