<html>

  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/timer.js"></script>
    
    <%!import json%>
  
    <script type="text/javascript">
      var pagehistory = { start:{time:new Date("${replay['time'].isoformat()}"),
                                 url:"${replay['url']}",
                                 html:${replay["htmlcontent"]|n}
                                },   
                          actions:${json.dumps(replay['actions'])|n},
                          mouseactions:${json.dumps(replay['mouseactions'])|n}
                        } 

      window.onload = function(){
          fixTimes(pagehistory.actions);
          fixTimes(pagehistory.mouseactions);
          replay(pagehistory);
        }; 
    </script>
  </head>

  <body>
  </body>


</html>
