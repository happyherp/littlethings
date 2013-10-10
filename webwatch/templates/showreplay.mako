<html>

  <head>
    <script type="text/javascript" src="/static/common.js"></script>
    <script type="text/javascript" src="/static/player.js"></script>
    <script type="text/javascript" src="/static/jquery-2.0.3.min.js"></script>
  
    <script type="text/javascript">
      var pagehistory = { start:{time:new Date("${replay['time'].isoformat()}"),
                                 url:"${replay['url']}",
                                 html:${replay["htmlcontent"]|n}
                                },   
                          actions:[],
                          mousemoves:[]
                        } 


       $().ready(function(){replay(pagehistory)}); 
    </script>
  </head>

  <body>
  </body>


</html>
