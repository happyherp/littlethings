/**
* Plays an entire session of a user that contains multiple replays of single pages.
*
*/


function Sessionplayer(session){
  
  this.session = session;
  
  this.pageplayer;
  
  this.replay = function(){
    console.log("replaying session:", session);
    
    this.pageplayer = new Player(session.recordings[0]);
    this.pageplayer.replay();
  };
  
}


function fixTimesInSession(session){
  for (var i = 0; i<session.recordings.length;i++){
    fixTimesInPagehistory(session.recordings[i]);
  }
  
}