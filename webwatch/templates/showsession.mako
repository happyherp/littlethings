<html>
  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    <script type="text/javascript" src="/static/models.js"></script>
    <script type="text/javascript" src="/static/sessionplayer.js"></script>
    <script type="text/javascript" src="/static/serverloader.js"></script>
    <script type="text/javascript" src="/static/URI.js"></script>
    
    
    <%!import json%>
    
    
    <script type="text/javascript">
      var session = Session.fromJSON(${json.dumps(session)|n});
      
      var sessionplayer = new Sessionplayer(session);
      var serverloader = new Serverloader(sessionplayer);
      
      //Adjust offset(delay) so that it matches our polling interval and
      //the replay stays smooth.
      sessionplayer.minimumOffset = Math.floor(serverloader.pollingInterval * 1.5);
      
      window.onload = function(){
      
	      sessionplayer.replay();      
	      serverloader.start();
      
      };
      
    </script>
    
    
  </head>
<body>

<!-- Recorded content will be displayed in there. This is needed to simulate the 
original window-size. -->
<iframe src="/static/blank.html" id="watchframe" width="500" height="600" name="watchframe" notrelevant="1" >
</iframe>

</body>
</html>