function testLoad(changes){

  //reset cookie   
  setCookie("webwatchsession", "", 1);

  setupRecorder();
  
  //State of cookie is saved in recorder.. reset so nothing more is added
  // to the session.
  setCookie("webwatchsession", "", 1);
  
  //Trigger recorder.
  var div = document.createElement("div");
  document.body.appendChild(div);
  document.body.removeChild(div);

  
  //Wait for initial request to be send to server.
  serversender.onReceive.once(function(){
    console.log("initial push to server came back.");
    if (recorder.pagehistory.sessionId){
      replaywindow = open("/showSession/"+recorder.pagehistory.sessionId+"/");
      
      //Wait for session-site to load.
      replaywindow.onload = function(){
        console.log("replaywindow loaded.");
        replaywindow.serverloader.stop();
        
        var onqueuedone = function(){
          console.log("sessionplayer focus queue was processed.");
          if (!compareNodes(document.body,replaywindow.document.body)){
            console.error("Nodes did not match.");
          }else{
            doChanges(replaywindow, changes);
          }
        };
        
        if (replaywindow.sessionplayer.focustimer.queue.length == 0){
          onqueuedone();
        }else{        
          replaywindow.sessionplayer.focustimer.onStopProcessing.once(onqueuedone);
        }
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
    
    //Prevent the watcher from loading our changes to early.
    replaywindow.serverloader.stop();
    
    var change = changes.shift();
    change();
    console.log("did the change.");
    //Wait until the change has reached the server.
    this.serversender.onReceive.once(function(){
      
      console.log("change was send to server.");
      replaywindow.serverloader.start();
      
    
      replaywindow.serverloader.onNewData.once(function(){
        replaywindow.serverloader.stop();
        
        console.log("watcher reveived data.");
        
        var onchangesreplayed = function(){
          console.log("watcher did the change Comparing nodes.");
          if (!compareNodes(document.body, replaywindow.document.body)){
            console.error("Nodes did not match.");
          }else{          
            doChanges(replaywindow, changes);
          }  
        };
        
        //Wait until the changes have been processed.
        if (replaywindow.sessionplayer.pageplayer.timer.queue.length == 0){
          onchangesreplayed();
        }else{
          replaywindow.sessionplayer.pageplayer.timer.onStopProcessing.once(onchangesreplayed);
        }
 
      }); 
    });
  }else{
    console.log("all passed.");
  }
}
 
  