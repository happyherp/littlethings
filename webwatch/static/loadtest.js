function testLoad(changes){

  //reset cookie   
  setCookie("webwatchsession", "", 1);

  setupRecorder();
  
  //State of cookie is saved in recorder.. reset so nothing more is added
  // to the session.
  setCookie("webwatchsession", "", 1);

  
  //Wait for initial request to be send to server.
  serversender.onReceive.once(function(){
    console.log("initial push to server came back.");
    if (recorder.pagehistory.sessionId){
      replaywindow = open("/showSession/"+recorder.pagehistory.sessionId+"/");
      
      //Wait for session-site to load.
      replaywindow.onload = function(){
        console.log("replaywindow loaded.");
        
        replaywindow.sessionplayer.focustimer.onStopProcessing.once(function(){
          console.log("sessionplayer focus queue was processed.");
          if (!compareNodes(document.body,replaywindow.document.body)){
            console.error("Nodes did not match.");
          }
          doChanges(replaywindow, changes);
        });
      };  

    }else{
      log.error("no session id found.");
    } 
  });
} 


/**
 * Does all of the changes in the current window. After each one, 
 * wait for replaywindow and check if the change was correclty done.
 * 
 * @param replaywindow
 * @param changes
 */
function doChanges(replaywindow, changes){
  
  if (changes.length != 0){
    
    var change = changes.shift();
    change();
    
    replaywindow.serverloader.onNewData.once(function(){
      console.log("received new data. Comparing nodes.");
      if (!compareNodes(document.body,replaywindow.document.body)){
        console.error("Nodes did not match.");
      }
      
      doChanges(replaywindow, changes);
      
    }); 
  }else{
    console.log("all passed.");
  }
}
 
  