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

      self.withClient(lambda req, flowid:(Response("iter: "+str(i)+" flow: "+str(flowid)
             +'You said: '+ req.params["in"]
             +'<form action="/followFlow/'+str(flowid)+'">'
                +'<input type="text" name="in" />'
                +'<input type="submit" value="continue" />'
             +'</form >'),None)) 

      i += 1

  def withClient(self,foo):
    '''uses the given function to process then next request from the client. 
    Function must be like 
    def foo(request):
      return (response, result)'''
    global flowcount, openflows

    flowid = flowcount

    openflows[flowid] = self
    flowcount += 1

    print "withclient", openflows
    req = self.channel.receive()
    print "withClient got request"

    response, retval = foo(req,flowid)
    self.channel.send(response)

    print "withclient end", openflows
    return retval


def food(request):
   
  foods = ['cheese', 'apples']

  
  response = startFlow(request, EchoFlow())

  #request, selected = chooseOne(request,foods) 
  #Response("You picked " + selected )

  return response

def startEcho(request):
  return startFlow(request, EchoFlow())


#Options is a list of things
def chooseOne(request, options):
  #display website for choosing

  #wait for response from user

  #parse result

  #return (request, answer)

  return (request, options[0])

def startFlow(request, flow):
  print "startFlow"

  #start new microthread containing the flow.
  stackless.tasklet(flow.run)()

  #process first page to display.
  flow.channel.send(request)
  response = flow.channel.receive()

  print "startFlow END"

  return response


def followFlow(request):
  print "followFlow", openflows
  flowid = int(request.matchdict['id'])
  flow = openflows[flowid]
  flow.channel.send(request)
  response = flow.channel.receive()
  return response
   

if __name__ == '__main__':
  config = Configurator()

  config.add_route('food', '/food')
  config.add_view(food, route_name='food') 

  config.add_route('echo', '/echo')
  config.add_view(startEcho, route_name='echo') 

  config.add_route('followFlow','/followFlow/{id}')
  config.add_view(followFlow, route_name='followFlow')

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  #run the server inside a tasklet
  stackless.tasklet(server.serve_forever)()  
  stackless.run()
  
