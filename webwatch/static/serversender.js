


/**
 * 
 * Class for Sending the information collected by the recorder to the server.
 * 
 */
function Serversender(recorder){
  
  this.recorder = recorder;
  
  this.send_loop_on = false;
  
  /**
   * Keeps track of how much information have already been sent to the server. 
   */
  this.sendCount = new PagemodificationCount(this.recorder.pagehistory.modifications);
  
  this.firstSend = true;
  
  /**
   * Fires when we get a response from the server after we sent new data.
   * 
   */
  this.onReceive = new Event();
  
  
  this.startSendLoop = function(){
    this.send_loop_on = true;
    this.__sendloop();
  };
  
  this.__sendloop = function(){
    if (this.send_loop_on ){
      if (this.__newdata()){
        this.sendToServer();
      }
      window.setTimeout(this.__sendloop.bind(this), 500);
    }
  };
  
  /**
   * returns true if there is new data to be send to the server.
   */
  this.__newdata = function(){
    return this.sendCount.isOlder(this.recorder.pagehistory.modifications); 
  };

  this.sendToServer = function(){
    var _this = this;
    
    //Send initial State
    if (this.firstSend){      
      console.log("sending initial state to server", this.recorder.pagehistory);
      
      var _recorder = this.recorder;
      var callback =  function(text){
        console.log("gotresponse", text);
        var response = JSON.parse(text);
        _recorder.pagehistory.id = response.newid;
        console.log("new id:", _recorder.pagehistory.id); 
        _this.onReceive.fire();
      };      
      
      var data ={pagehistory:this.recorder.pagehistory, count:this.sendCount};      
      post("/receiveReplay", JSON.stringify(data), callback); 
      this.firstSend = false;
      this.__updateSendCount();
      
    //Send Update  
    }else if (this.recorder.pagehistory.id){     
      console.log("sending update to server. ");      
      var newdata = {
          modifications : this.recorder.pagehistory.modifications.getNewerModifications(this.sendCount),
          id            : this.recorder.pagehistory.id,
          count         : this.sendCount
      };
      
      var callback =  function(text){
        console.log("gotresponse", text);
        _this.onReceive.fire();        
      };
      post("/receiveReplayUpdate", JSON.stringify(newdata), callback);
      this.__updateSendCount();
      
    }else{
      console.log("Not sending update because we still have no id.");
    }
  };
  
  this.__updateSendCount = function(){
    this.sendCount = new PagemodificationCount(this.recorder.pagehistory.modifications);  
  };  
  
}