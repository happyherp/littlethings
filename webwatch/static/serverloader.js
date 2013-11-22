
/**
 * Loads new data from the server and updates a player with it.
 * 
 */
function Serverloader(sessionplayer){
  
  this.sessionplayer = sessionplayer;
  
  this.onNewData = new Event();
  
  this.start = function(){
    this.__load();
  };
  
  this.__load = function(){
    var data = [];
    
    for (var i = 0 ; i < this.sessionplayer.session.recordings.length;i++){
      var recording = this.sessionplayer.session.recordings[i];

      var recorddata = {count:{dom:  recording.actions.length,
                                mouse: recording.mouseactions.length, 
                                focus: recording.focus.length},
                         id : recording.id};
                  
      data.push(recorddata);      
    }
    
    post("/getSessionUpdate", JSON.stringify(data), this.__processResponse.bind(this));
        
  };
    
  this.__processResponse = function(response){
  
    var session_update = JSON.parse(response);
    console.log("got update answer: ", session_update);
    
    //Fix times.
    for (var i = 0; i<session_update.recording_updates.length;i++){
      var update = session_update.recording_updates[i];
      fixTimes(update.actions);
      fixTimes(update.mouseactions);
      fixTimes(update.focus);
    }
    
    this.sessionplayer.loadNewData(session_update);
    
    this.onNewData.fire(session_update);
    
    window.setTimeout(this.__load.bind(this), 300);
      
  };
  
}