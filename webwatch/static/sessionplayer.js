/**
* Plays an entire session of a user that contains multiple replays of single pages.
* 
* 
*
*/


function Sessionplayer(session){
  
  this.session = session || null;
  
  /**
   * This array keeps track of all the focus changes in all of the pages 
   * of this session.
   */
  this.focus = [];
  
  this.focustimer = new Timer();
  
  //Offset in milliseconds from the original recording.
  this.offset = 0;
  
  this.pageplayer;
  
  this.replay = function(){
    console.log("replaying session:", session);
    
    this.__insertPageFocus();    
    
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
  
  /**
   * 
   * loads an Update into the player.
   * 
   * @param {array of Pagemodifications}modifications_array
   * 
   */
  this.loadNewData = function(modifications_array){
    //TODO: process focus once its available.
    
    for (var i = 0; i<modifications_array.length;i++){
      var modifications = modifications_array[i];
      
      //Let current player react to update.
      if (this.pageplayer && this.pageplayer.history.id == modifications.id){
        this.pageplayer.addActionsToTimer(modifications); 
      }

      //Add to Session-data
      var page = getPageById(modifications.id);
      page.modifications.add(modifications);

      
    }
    
  };
  
  /**
   * insert starttimes of recordings into focus so we switch
   * to the new page, when it was opened.
   */
  this.__insertPageFocus = function(){
        
    for (var i = 0; i<this.session.pages.length; i++){
      
      var newfocus = {
          time:this.session.pages[i].time,
          record_id : this.session.pages[i].id
      };
      
      var j = 0;
      while (j<this.focus.length 
               && this.focus[j].time < newfocus.time ){j++;}
      
      this.focus.splice(j,0,newfocus);
    }    
  };
  
  function switchFocus(focus){
    
    if (!this.pageplayer){
      this.pageplayer = new Player(getPageById(focus.record_id));
      this.pageplayer.offset = this.offset;
      this.pageplayer.replay();
    }else if (focus.record_id != this.pageplayer.history.id) {
      this.pageplayer.stop();
      this.pageplayer = new Player(getPageById(focus.record_id));
      this.pageplayer.offset = this.offset;      
      this.pageplayer.replay();
    }else{
      console.log("got new focus, but was on same page as last one.");
    }
  }
  
  function getPageById(id){
    for (var i = 0; i<this.session.pages.length; i++){
      if (this.session.pages[i].id == id ){
        return this.session.pages[i];
      }
    }
    console.error("Could not find recording with id ", id);
    
  }
  
}