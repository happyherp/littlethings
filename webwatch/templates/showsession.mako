<html>
  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    <script type="text/javascript" src="/static/sessionplayer.js"></script>
    <script type="text/javascript" src="/static/serverloader.js"></script>
    
    <%!import json%>
    
    
    <script type="text/javascript">
      var session = ${json.dumps(session)|n};
      fixTimesInSession(session);
      
      var focus = ${json.dumps(focus)|n};
      fixTimes(focus);
      
      
      var sessionplayer = new Sessionplayer(session, focus);
      sessionplayer.replay();
      
      var serverloader = new Serverloader(sessionplayer);
      serverloader.start();
      
    </script>
    
    
  </head>
<body></body>
</html>