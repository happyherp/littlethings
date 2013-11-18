/**
 * A Map that can use Node-Objects as keys. 
 * 
 * Linear Search time... :-/
 */


function Map(inkeys, invalues){
  
  this.keys = inkeys?inkeys.slice():[];
  this.values = invalues?invalues.slice():[];
  
  
  this.set = function(key, value){
    var index = this.keys.indexOf(key);
    if (index == -1){
      this.keys.push(key);
      this.values.push(value);
    }else{
      this.values[index] = value;
    }
  };
  
  this.get = function(key){
    var index = this.keys.indexOf(key);
    if (index == -1){
      return undefined;
    }else{
      return this.values[index];
    }    
  };
  
  /**
   * Copies the association. keys and values are not deep-copied.
   * 
   */
  this.copy = function(){
    return new Map(this.keys,this.values);
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
  
  
  map2 = map.copy();
  map2.set(1,"r");
  map2.set(3, '3');
  
  ok = ok && map2.get(2) == "k";
  ok = ok && map.get(1) == "p";
  ok = ok && map.get(3) == undefined;
  
  
  if (ok){
    console.log("all fine");
  }else{
    console.error("maptest failed");
  }
}