/**
* Plays an entire session of a user that contains multiple replays of single pages.
* 
* 
*
*/


function Sessionplayer(session, focus){
  
  this.session = session || null;
  
  this.focus = focus || [];
  
  this.focustimer = new Timer();
  
  //Offset in milliseconds from the original recording.
  this.offset = 0;
  
  this.pageplayer;
  
  this.replay = function(){
    console.log("replaying session:", session);
    
    insertPageFocus();    
    
    //Set offset into the future such that first action happens immediatly.
    this.offset = new Date().getTime() - this.focus[0].time.getTime();
    
    //Set up the timer so the content gets switched to the one that got focus.
    for (var i = 0; i<this.focus.length; i++){
      var callback = saveState(switchFocus.bind(this),this.focus[i]);
      var runat =  new Date(this.focus[i].time.getTime() + this.offset);
      this.focustimer.addEvent(callback,runat);
    }
    
    this.focustimer.run();
    
  };
  
  this.loadNewData = function(session_update){
    //TODO: process focus once its available.
    
    for (var i = 0; i<session_update.recording_updates.length;i++){
      var recording_update = session_update.recording_updates[i];
      
      //Let current player react to update.
      if (this.pageplayer && this.pageplayer.history.id == recording_update.id){
        this.pageplayer.addActionsToTimer(recording_update); 
      }

      //Add to Session-data
      var record = getRecordById(recording_update.id);
      record.actions = record.actions.concat(recording_update.actions);
      record.mouseactions = record.mouseactions.concat(recording_update.mouseactions);
      record.focus = record.focus.concat(recording_update.focus);
      
    }
    
  };
  
  /**
   * insert starttimes of recordings into focus so we switch
   * to the new page, when it was opened.
   */
  function insertPageFocus(){
        
    for (var i = 0; i<this.session.recordings.length; i++){
      
      var newfocus = {
          time:this.session.recordings[i].start.time,
          record_id : this.session.recordings[i].id
      };
      
      var j = 0;
      while (j<this.focus.length 
               && this.focus[j].time < newfocus.time ){j++;}
      
      this.focus.splice(j,0,newfocus);
    }    
  };
  
  function switchFocus(focus){
    
    if (!this.pageplayer){
      this.pageplayer = new Player(getRecordById(focus.record_id));
      this.pageplayer.offset = this.offset;
      this.pageplayer.replay();
    }else if (focus.record_id != this.pageplayer.history.id) {
      this.pageplayer.stop();
      this.pageplayer = new Player(getRecordById(focus.record_id));
      this.pageplayer.offset = this.offset;      
      this.pageplayer.replay();
    }else{
      console.log("got new focus, but was on same page as last one.");
    }
  }
  
  function getRecordById(id){
    for (var i = 0; i<this.session.recordings.length; i++){
      if (this.session.recordings[i].id == id ){
        return this.session.recordings[i];
      }
    }
    console.error("Could not find recording with id ", id);
    
  }
  
}

/**
 * replace strings with dates for all session-objects.
 * 
 * @param session
 */
function fixTimesInSession(session){
  for (var i = 0; i<session.recordings.length;i++){
    fixTimesInPagehistory(session.recordings[i]);
  }  
}