<html>

  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    
    <%!import json%>
  
    <script type="text/javascript">
      var pagehistory = ${recording_json|n}

      window.onload = function(){
          pagehistory.start.time = new Date(pagehistory.start.time);
          fixTimes(pagehistory.actions);
          fixTimes(pagehistory.mouseactions);
          replay(pagehistory);
        }; 
    </script>
  </head>

  <body></body>


</html>
