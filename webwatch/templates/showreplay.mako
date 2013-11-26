<html>

  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    <script type="text/javascript" src="/static/models.js"></script>
    
    <%!import json%>
  
    <script type="text/javascript">
      var pagehistory = ${json.dumps(recording)|n}

      window.onload = function(){
          player = new Player(Pagehistory.fromJSON(pagehistory));
          player.replay();
        }; 
    </script>
  </head>

  <body></body>


</html>
