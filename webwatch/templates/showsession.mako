<html>
  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    <script type="text/javascript" src="/static/models.js"></script>
    <script type="text/javascript" src="/static/sessionplayer.js"></script>
    <script type="text/javascript" src="/static/serverloader.js"></script>
    
    <%!import json%>
    
    
    <script type="text/javascript">
      var session = Session.fromJSON(${json.dumps(session)|n});
      
      var sessionplayer = new Sessionplayer(session);
      sessionplayer.replay();
      
      var serverloader = new Serverloader(sessionplayer);
      serverloader.start();
      
    </script>
    
    
  </head>
<body></body>
</html>