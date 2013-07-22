from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response

import stackless

flowcount = 0

openflows = {}

class FoodFlow:
  
  def __init__(self):
    self.channel = stackless.channel()

  def run(self):
    request = self.channel.reveive()


class EchoFlow:

  def __init__(self):
    self.channel = stackless.channel()

  def run(self):
    global flowcount
    
    i = 1

    while True:
      print "Iteration", i, "flowcount", flowcount



      req = self.channel.receive()


      openflows[flowcount] = self
      self.channel.send(Response("iter: "+str(i)+" flow: "+str(flowcount)
             +'<a href="/followFlow/'+str(flowcount)+'">continue</a>'))
      flowcount += 1

      i += 1


def food(request):
   
  foods = ['cheese', 'apples' ]

  
  response = startFlow(request, EchoFlow())

  #request, selected = chooseOne(request,foods) 
  #Response("You picked " + selected )

  return response


#Options is a list of things
def chooseOne(request, options):
  #display website for choosing

  #wait for response from user

  #parse result

  #return (request, answer)

  return (request, options[0])

def startFlow(request, flow):

  #start new microthread containing the flow.
  stackless.tasklet(flow.run)()

  #process first page to display.
  flow.channel.send(request)
  response = flow.channel.receive()
  return response


def followFlow(request):
  flowid = int(request.matchdict['id'])
  flow = openflows[flowid]
  flow.channel.send(request)
  response = flow.channel.receive()
  return response
   

if __name__ == '__main__':
  config = Configurator()

  config.add_route('food', '/food')
  config.add_view(food, route_name='food') 

  config.add_route('followFlow','/followFlow/{id}')
  config.add_view(followFlow, route_name='followFlow')

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  #run the server inside a tasklet
  stackless.tasklet(server.serve_forever)()  
  stackless.run()
  

  #server.serve_forever()
