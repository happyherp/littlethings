from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response

import stackless
from webcmd import Flow, startFlow, followFlow, flowcount

class EchoFlow(Flow):

  def run(self):
    
    i = 1

    while True:
      print "Iteration", i, "flowcount", flowcount

      self.withClient(lambda req, flowid:(Response("iter: "+str(i)+" flow: "+str(flowid)
             +'You said: '+ req.params["in"]
             +'<form action="/followFlow/'+str(flowid)+'">'
                +'<input type="text" name="in" />'
                +'<input type="submit" value="continue" />'
             +'</form >'),None)) 

      i += 1

def startEcho(request):
  return startFlow(request, EchoFlow())




if __name__ == '__main__':
  config = Configurator()

  config.add_route('echo', '/echo')
  config.add_view(startEcho, route_name='echo') 

  config.add_route('followFlow','/followFlow/{id}')
  config.add_view(followFlow, route_name='followFlow')

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  #run the server inside a tasklet
  stackless.tasklet(server.serve_forever)()  
  stackless.run()
