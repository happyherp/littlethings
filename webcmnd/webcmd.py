from pyramid.response import Response

import stackless

flowcount = 0

openflows = {}

class Flow:

  def __init__(self):
    self.channel = stackless.channel()

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

    del openflows[flowid]

    print "withclient end", openflows
    return retval

class FoodFlow:
  
  def __init__(self):
    self.channel = stackless.channel()

  def run(self):
    request = self.channel.reveive()



def food(request):
   
  foods = ['cheese', 'apples']

  
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
   

  
