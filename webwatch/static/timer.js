/**
 * Module for Timing events.
 */

function Timer() {

  /**
   * Contains {date:.., callback..} objects sorted by date asc.
   */
  this.queue = [];
  
  this.running = false;  
  
  this.waitingForProcessing = false;
  
  this.onStopProcessing = new Event();
  
  this.__processQueue = function (){    
    this.waitingForProcessing = false;
    while (this.running && this.queue.length > 0 && this.queue[0].date < new Date()){
      var event = this.queue.shift();
      event.callback();
    }
    this.__continueProcessing();
    
  };
  
  this.__continueProcessing = function (){
    if (this.running && !this.waitingForProcessing){
      
      if ( this.queue.length == 0 ){
        this.onStopProcessing.fire();
      }else{        
        this.waitingForProcessing = true;
        window.setTimeout(this.__processQueue.bind(this), 50);        
      }

    };
  };
    
  /**
   * Starts the execution of the Queue.
   */
  this.run = function (){    
    this.running = true;
    this.__continueProcessing();
  };
  
  this.stop = function(){
    this.runnung = false;
  }

  /**
   * Call the given function on the given time. Calls instantly if date is in
   * the past.
   * 
   */
  this.addEvent = function(callback, date) {
    var newEvent = {
      callback : callback,
      date : date
    };
    if (this.queue.length == 0) {
      this.queue.push(newEvent);
    } else {
      // Do a sorted insert.
      //It is important, that for same date value, the order of events is the same 
      //as the order in which addEvent was called for those events.
      //Otherwhise DOM-elements might be inserted or deleted at the wrong position.
      var i = 1;        
      while (i < this.queue.length  && !(this.queue[i].date > date)) {
        i++;
      }
      this.queue.splice(i,0,newEvent);
    }
    this.__continueProcessing();
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
