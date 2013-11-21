


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
  this.sendCount = {first:true,dom:0,mouse:0, focus:0};
  
  
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
    return !(this.sendCount.dom ==  this.recorder.pagehistory.actions.length
     && this.sendCount.mouse == this.recorder.pagehistory.mouseactions.length
     && this.sendCount.focus == this.recorder.pagehistory.focus.length); 
  };

  this.sendToServer = function(){
    
    
    if (this.sendCount.first){      
      console.log("sending initial state to server", this.recorder.pagehistory);
      
      var _recorder = this.recorder;
      var callback =  function(text){
        console.log("gotresponse", text);
        var response = JSON.parse(text);
        _recorder.pagehistory.id = response.newid;
        console.log("new id:", _recorder.pagehistory.id);        
      };      
      
      var data ={actions:this.recorder.pagehistory, count:this.sendCount};      
      post("/receiveReplay", JSON.stringify(data), callback);      
      this.__updateSendCount();
      
    }else if (this.recorder.pagehistory.id){     
      console.log("sending update to server. ");      
      var newdata = {actions: {actions     : this.recorder.pagehistory.actions.slice(this.sendCount.dom),
                               mouseactions: this.recorder.pagehistory.mouseactions.slice(this.sendCount.mouse),
                               focus       : this.recorder.pagehistory.focus.slice(this.sendCount.focus),
                               id          : this.recorder.pagehistory.id},
                     count:this.sendCount};
      
      var callback =  function(text){
        console.log("gotresponse", text);
      };
      post("/receiveReplayUpdate", JSON.stringify(newdata), callback);
      this.__updateSendCount();
      
    }else{
      console.log("Not sending anything because we still have no id.");
    }
  };
  
  this.__updateSendCount = function(){
    this.sendCount = {first: false,
        dom:   this.recorder.pagehistory.actions.length,
        mouse: this.recorder.pagehistory.mouseactions.length, 
        focus: this.recorder.pagehistory.focus.length};    
  };  
  
}