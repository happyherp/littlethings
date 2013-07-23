from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response

import stackless
from webcmd import Flow, startFlow, followFlow, flowcount

def startFlowView(flow):
  return lambda req:startFlow(req, flow())



class EchoFlow(Flow):

  def run(self, request):
    
    i = 1

    while True:
      print "Iteration", i, "flowcount", flowcount

      request = self.sendRead(lambda flowid:
            Response( "iter: "+str(i)+" flow: "+str(flowid)
                      +'You said: '+ request.params["in"]
                      + self.inForm(flowid, '<input type="text" name="in" />'
                                           +'<input type="submit" value="continue" />'))) 
      i += 1


class HelloFlow(Flow):
  
  def run(self,request):
    return Response("Hello there")

class MoreFlow(Flow):
  
  def run(self, request):
    while self.confirm("BAM. One more time?"):
      pass
    return Response("Done already")


class SmallTalkFlow(Flow):
  
  def run(self, request):
    name = self.input("Wie heisst du?")
    while name == "" or not self.confirm("Heisst du wirklich "+name+"?"):
      name = self.input("Geb doch endlich deinen namen ein!")

    self.showMessage("Hallo %s!" %(name));

    alter = None
    alter_s = self.input("Wie Alt bist du?")
    while alter==None:
      try: 
        alter = int(alter_s)
      except:
        alter_s = self.input("Bitte eine Zahl. Wie Alt bist du?")


    favfoods = self.select(["Pizza", "Lasagne", "Hamburger", "Salat"], 
                           message="Was hiervon isst du gerne?")


    summary ='''Also nochmal: 
                Dein name ist %s und du bist %d Jahre alt. 
                Du isst gerne %s''' %(name, alter, favfoods)
    return Response(summary)

if __name__ == '__main__':
  config = Configurator()

  config.add_route('hello', '/hello')
  config.add_view(startFlowView(HelloFlow), route_name='hello') 

  config.add_route('echo', '/echo')
  config.add_view(startFlowView(EchoFlow), route_name='echo') 

  config.add_route('more', '/more')
  config.add_view(startFlowView(MoreFlow), route_name='more')

  config.add_route('small', '/small')
  config.add_view(startFlowView(SmallTalkFlow), route_name='small')

  config.add_route('followFlow','/followFlow/{id}')
  config.add_view(followFlow, route_name='followFlow')

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  #run the server inside a tasklet
  stackless.tasklet(server.serve_forever)()  
  stackless.run()
