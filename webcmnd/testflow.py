from wsgiref.simple_server import make_server
from pyramid.config import Configurator
from pyramid.response import Response
from pyramid.renderers import render_to_response, render

import stackless

from random import randint
import os

from webcmd import Flow, startFlow, followFlow, flowcount
from HTMLControls import *


def startFlowView(flow):
  return lambda req:startFlow(req, flow())

class FormInTemplateFlow(Flow):
  '''This flow wraps the form into a template'''

  def inForm(self, content):
    withform = Flow.inForm(self, content)
    return render('design.mako', {'content':withform})


class EchoFlow(Flow):

  def run(self, request):   
    i = 1
    while True:
      print "Iteration", i, "flowcount", flowcount
      request = self.sendRead(Response( 
                      "iter: %d flow:%d step:%d" %(i, self.flowid, self.step)
                      +'You said: '+ request.params["in"]
                      + self.inForm('<input type="text" name="in" />'
                                    +'<input type="submit" value="continue" />'))) 
      i += 1


class HelloFlow(Flow):
  
  def run(self,request):
    return showMainPage(request, "Hello there")

class MoreFlow(Flow):
  
  def run(self, request):
    while self.confirm("BAM. One more time?"):
      pass
    return Response("Done already")


class SmallTalkFlow(FormInTemplateFlow):
  
  def run(self, request):

    name = self.input("What's your name?")
    while name == "" or not self.confirm("Did your Parents really name you "+name+"?"):
      name = self.input("Come on. Put your name in!")
    self.showMessage("Hallo %s!" %(name));

    age = None
    age_s = self.input("How old are you?")
    while age==None:
      try: 
        age = int(age_s)
      except:
        age_s = self.input("Please enter a number. How old are you?")

    favfoods = self.select(["Pizza", "Lasagne", "Hamburger", "Salad"], 
                           message="Which of these foods do you like?")

    summary ='''So: Your name is %s and you are %d years old. 
                You like to eat %s''' %(name, age, favfoods)
    return Response(summary)

class GuessFlow(Flow):
  def run(self, request):
    number = randint(1,100)

    i = 1
    guess = self.input_number("Guess the number.")
    while guess != number:
      if guess > number:
        info = "The number is lower."
      else:
        info = "The number is higher."
      guess = self.input_number(info + "<br /> Guess again.")
      i += 1

    return Response("You Got it. The number was %d and it took you %d tries to figure it out." 
                    %(number, i))


class ReverseGuessFlow(Flow):
  def run(self, request):
    self.showMessage('You think of a number between 1 and 100 and I will guess it!')
    return self.guess(1,100)

  def guess(self, low, high):    
    number = (low+high) / 2  
    if self.confirm("Is it %d?"%(number)):
      return Response("Yay")
    elif low == high:
      return Response("Come on! You Cheated!")
    elif self.confirm("Is it higher than %d?" %(number)):
      return self.guess(number+1, high)
    else:
      return self.guess(low,number-1)

class FormFlow(Flow):
  def run(self, request):
    
    form = [Text("FirstName"), Input(),
            Text("Last name"), Input(),
            Text("Hobbys"), Select(["Basketball", "Badminton", "Cheesecakes"]),
            Button("ok"), 
            Button("cancel")]

    result = self.doForm(form)
    print "FormFlow", result
    while result[7]:
      self.showMessage("canceling is not an option")
      result = self.doForm(form)

    #grabbing results by index is not nice. Acces should happen over some kind of name.
    return Response("You are %s %s. Your hobbys are %s."%(result[1], result[3], result[5]))


def showMainPage(request, message = ""):
  response = render_to_response('main.mako', {'message':message}, request=request)
  return response


if __name__ == '__main__':

  here = os.path.dirname(os.path.abspath(__file__))

  settings = {}
  settings['reload_all'] = True
  settings['debug_all'] = True
  settings['mako.directories'] = os.path.join(here, 'templates')
  

  config = Configurator(settings=settings)

  config.add_route('start', '/')
  config.add_view(showMainPage, route_name='start')

  config.add_route('hello', '/hello')
  config.add_view(startFlowView(HelloFlow), route_name='hello') 

  config.add_route('echo', '/echo')
  config.add_view(startFlowView(EchoFlow), route_name='echo') 

  config.add_route('more', '/more')
  config.add_view(startFlowView(MoreFlow), route_name='more')

  config.add_route('small', '/small')
  config.add_view(startFlowView(SmallTalkFlow), route_name='small')

  config.add_route('guess', '/guess')
  config.add_view(startFlowView(GuessFlow), route_name='guess')

  config.add_route('reverseguess', '/reverseguess')
  config.add_view(startFlowView(ReverseGuessFlow), route_name='reverseguess')

  config.add_route('form', '/form')
  config.add_view(startFlowView(FormFlow), route_name='form')

  config.add_route('followFlow','/followFlow/{id}/{step}')
  config.add_view(followFlow, route_name='followFlow')

  app = config.make_wsgi_app()
  server = make_server('0.0.0.0', 8080, app)

  server.serve_forever()
