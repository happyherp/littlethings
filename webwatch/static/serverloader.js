
/**
 * Loads new data from the server and updates a player with it.
 * 
 */
function Serverloader(sessionplayer){
  
  this.sessionplayer = sessionplayer;
  
  this.running = false;
  
  this.windowhasfocus = false;
  
  
  this.onNewData = new Event();
  
  this.pollingIntervalMS = 500;
  
  this.start = function(){
    this.running = true;
    this.__load();
    
    window.addEventListener("focus",function(){
      this.windowhasfocus = true;
    }.bind(this));
    
    window.addEventListener("blur",function(){
      this.windowhasfocus = false;
    }.bind(this));    
    
    
    
  };
  
  this.stop = function(){
    this.running = false;
  };
  
  this.__load = function(){
    if (this.running && this.windowhasfocus){
      var data = [];
      
      for (var i = 0 ; i < this.sessionplayer.session.pages.length;i++){
  
        var page = this.sessionplayer.session.pages[i];
        
        var recorddata = {
            count:new PagemodificationCount(page.modifications),
            id:page.id
        };
                    
        data.push(recorddata);      
      }
      console.log("serverloading requests new data.", data);
      post("/getSessionUpdate", JSON.stringify(data), this.__processResponse.bind(this));
      
    }else if (this.running){
      //We don't have focus right now. try again.
      window.setTimeout(this.__load.bind(this),40);
    }
  };
    
  this.__processResponse = function(response){
  
    if (this.running){
      var session_update_raw = JSON.parse(response);
      var session_update = {
          modifications:session_update_raw.page_updates.map( Pagemodifications.fromJSON)
          };
      
      console.log("got update answer: ", session_update);
      
      this.sessionplayer.loadNewData(session_update.modifications);
      
      this.onNewData.fire(session_update);
      
      window.setTimeout(this.__load.bind(this), this.pollingIntervalMS);
    }
  };
  
}