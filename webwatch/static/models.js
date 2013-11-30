
/**
 * Container for data recorded during a session.
 * 
 */
function Session(){
  
  this.id = null;
  
  /**
   * List of Pagehistory.
   */
  this.pages = [];
}

Session.fromJSON = function(json_session){
  var session = new Session();
  session.id = json_session.id;
  session.pages = json_session.pages.map(Pagehistory.fromJSON);
  return session;
};


/**
 * Container for all data recorded on a single page. 
 * 
 */
function Pagehistory(){
    
  /**
   * DOM-State when recording was startet.
   */
  this.starthtml = null;
  
  this.time = null;
  
  this.url = null;
  
  this.modifications = new Pagemodifications();
   
  this.sessionId = null;
  
  this.id  =  null;
  
}

Pagehistory.fromJSON = function(json_pagehistory){
  var pagehistory = new Pagehistory();
  pagehistory.time = new Date(json_pagehistory.time);
  pagehistory.modifications = Pagemodifications.fromJSON(json_pagehistory.modifications);
  copyAttributes(json_pagehistory, pagehistory, "starthtml", "url", "sessionId", "id");
  return pagehistory;
};



modificationtypes = ["dom", "mouse", "focus", "scroll"];

/**
 * All modifications that happen while the user is on the same site.
 * 
 */
function Pagemodifications(){
  /**
   * List of all DOM-Modification.
   */
  
  modificationtypes.map(function(type){
    this[type+"actions"] = [];
  }, this);
  
  
  this.pageid = null;
  
  /**
   * Returns a new Pagemodifications-Object containing only
   * modifications that were added after the count was made.
   * 
   * @param {PagemodificationCount} count
   * 
   */
  this.getNewerModifications = function(count){
    
    var mod = new Pagemodifications();
    modificationtypes.map(function(type){
      mod[type+"actions"] = this[type+"actions"].slice(count[type+"actions"]);
    }, this);    

    return mod;
  };
  
  /**
   * At all actions to the end of the actions of this modification.
   * 
   */
  this.add = function(modifications){
    
    modificationtypes.map(function(type){
      this[type+"actions"] = this[type+"actions"].concat(modifications[type+"actions"]);
    }, this);    
 
  };
  
}

Pagemodifications.fromJSON = function (json_modifications){
  var modifications = new Pagemodifications();
  copyAllAttributes(json_modifications, modifications);
  
  modificationtypes.map(function(type){
    fixTimes(modifications[type+"actions"]);
  }, this); 
  
  return modifications;
};

/**
 * Counter for Pagemodifications. 
 * 
 * When recording used to know how much
 * data has already been send to the server. 
 * 
 *  When replaying used to tell the server how much data we already have.
 *  
 *  @param {Pagemodifications} pagemodifications
 * 
 */
function PagemodificationCount(pagemodifications){
  
  modificationtypes.map(function(type){
    this[type+"actions"] = pagemodifications[type+"actions"].length;
  }, this); 
  
  
  /**
   * Returns true if the count is older than the given pagemodifications.
   * 
   * @param {Pagemodifications} pagemodifications
   * @return {boolean}
   */
  this.isOlder = function(pagemodifications){
     
    var allSameLength = true;
    
    modificationtypes.map(function(type){
      allSameLength = allSameLength 
         && this[type+"actions"] == pagemodifications[type+"actions"].length;
    }, this); 
    return !allSameLength;   
  };
  
}
