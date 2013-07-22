from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response

flowcount = 0

class Flow:
  
  def __init__(self):
    pass

def food(request):
   
 foods = ['cheese', 'apples' ]

 request, selected = chooseOne(request,foods) 

 return Response("You picked " + selected )


#Options is a list of things
def chooseOne(request, options):
  #display website for choosing

  #wait for response from user

  #parse result

  #return (request, answer)

  return (request, options[0])

def startFlow(request, flow):
  flow.channel.send(request)
  response = flow.channel.receive()
  return response
   

if __name__ == '__main__':
  config = Configurator()

  config.add_route('food', '/food')
  config.add_view(food, route_name='food')    

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)
  server.serve_forever()
