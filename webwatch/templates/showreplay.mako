<html>

  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    
    <%!import json%>
  
    <script type="text/javascript">
      var pagehistory = ${recording_json|n}

      window.onload = function(){
          fixTimesInPagehistory(pagehistory);
          player = new Player(pagehistory);
          player.replay();
        }; 
    </script>
  </head>

  <body></body>


</html>
