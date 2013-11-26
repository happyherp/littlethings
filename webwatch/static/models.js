
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


/**
 * All modifications that happen while the user is on the same site.
 * 
 */
function Pagemodifications(){
  /**
   * List of all DOM-Modification.
   */
  this.domactions = [];
  
  this.mouseactions = [];
  
  this.focusactions = [];
  
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
    mod.domactions   = this.domactions.slice(count.domactions);
    mod.mouseactions = this.mouseactions.slice(count.mouseactions);
    mod.focusactions = this.focusactions.slice(count.focusactions);
    
    return mod;
  };
  
  /**
   * At all actions to the end of the actions of this modification.
   * 
   */
  this.add = function(modifications){
    this.domactions   = this.domactions.concat(modifications.domactions);
    this.mouseactions = this.mouseactions.concat(modifications.mouseactions);
    this.focusactions = this.focusactions.concat(modifications.focusactions);    
  };
  
}

Pagemodifications.fromJSON = function (json_modifications){
  var modifications = new Pagemodifications();
  copyAllAttributes(json_modifications, modifications);
  fixTimes(modifications.domactions);
  fixTimes(modifications.mouseactions);
  fixTimes(modifications.focusactions);
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
  
  this.domactions =   pagemodifications.domactions.length;
  this.mouseactions = pagemodifications.mouseactions.length;
  this.focusactions = pagemodifications.focusactions.length;
  
  
  /**
   * Returns true if the count is older than the given pagemodifications.
   * 
   * @param {Pagemodifications} pagemodifications
   * @return {boolean}
   */
  this.isOlder = function(pagemodifications){
    return !(this.domactions ==  pagemodifications.domactions.length
        && this.mouseactions == pagemodifications.mouseactions.length
        && this.focusactions == pagemodifications.focusactions.length);    
  };
  
}
