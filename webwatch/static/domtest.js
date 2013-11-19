/**
* For automated testing of dom-modifications.
*
*/
function runtests(mods){
  
  var modification = mods.pop();
  
  if (modification){    
    domtest(modification, function(){
      runtests(mods);
    });  
  }
  
}


function domtest(modification, continuation){
  
  continuation = continuation? continuation:function(){};
    
  var startstate = document.body.innerHTML;
  
  var recorder = new Recorder();
  recorder.record();
  
  //Do changes to dom.
  modification();
  
  afterDOMreacts(function(){

    recorder.stopRecording(); 
    
    var afterChanges = document.body.innerHTML;
    document.body.innerHTML = startstate; 
    
    var player = new Player(recorder.pagehistory);
    
    player.timer.onStopProcessing.handlers.push(function(){      
      if (afterChanges == document.body.innerHTML){
        console.log("OK!");
        document.body.innerHTML = startstate;
        continuation();
      }else{
        console.error("Changes could not be replayed.");
        console.log("expected:", afterChanges);
        console.log("!=");
        console.log("actual:", document.body.innerHTML);
      }
                
    });
    
    player.replay();

  });
}

function afterDOMreacts(foo){
  window.setTimeout(foo,200);
}


