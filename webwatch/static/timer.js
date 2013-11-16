/**
 * Module for Timing events.
 */

function Timer() {

  /**
   * Contains {date:.., callback..} objects sorted by date asc.
   */
  var queue = [];
  
  var running = false;
  
  
  function processQueue(){
    
    while (queue.length > 0 && queue[0].date < new Date()){
      var event = queue.shift();
      event.callback();
    }
    
    window.setTimeout(processQueue, 100);
    
  }
  
  /**
   * Starts the execution of the Queue.
   */
  this.run = function (){    
    if (!running){
      running = true;
      processQueue();
    }
  };

  /**
   * Call the given function on the given time. Calls instantly if date is in
   * the past.
   * 
   */
  this.addEvent = function(callback, date) {
    if (date < new Date()) {
      callback();
    } else {
      var newEvent = {
        callback : callback,
        date : date
      };
      if (queue.length == 0) {
        queue.push(newEvent);
      } else {
        // Do a sorted insert.
        //It is important, that, for same date value, the order of events is the same 
        //as the order in which addEvent was called fot those events.
        //Otherwhise DOM-elements, that have not yet been created will cause errors
        //while finding the right target.
        var i = 1;        
        while (i < queue.length  && !(queue[i].date > date)) {
          i++;
        }
        queue.splice(i,0,newEvent);
      }
    }
  };
}


function testtimer(){
  var ins = new Timer();
  for (var i = 0; i < 5;i++){
    
    var makefoo = function(i){
      return function(){console.log(i);};
      };
    ins.addEvent(makefoo(i), 
                 new Date(new Date().getTime()+(1000*i)));
  }
  
  
  for (var i = 0; i < 5;i++){
    
    var makefoo = function(i){
      return function(){console.log(i);};
      };
    ins.addEvent(makefoo(i), 
                 new Date(new Date().getTime()));
  }
  
  ins.run();
}
