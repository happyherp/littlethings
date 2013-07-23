from pyramid.response import Response

import stackless


#TODO: move those into some kind of container. global state is bad.
flowcount = 0 #should maybe be renamed to stepcount. As one flow can contain any number of steps.
openflows = {}

class Flow:
  '''Superclass for all Flows. subclasses must implement a run method. 
  Currently the only state for the class is the channel. 
  ''' 

  def __init__(self):
    self.channel = stackless.channel()

  def sendRead(self, response):
   '''Sends the given respons to the client, waits for the answer'''

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

  def inForm(self, flowid, content):
    '''Wraps the passed html string into form tags with the action pointing to followFlow'''
    return '<form action="/followFlow/'+str(flowid)+'">'+content +'</form>'

  def confirm(self, message):
    '''Lets the user anser a yes/no question'''
    def render(req, flowid):
      pass#html = self.inForm(flowid, <input>
   
    return withclient(render)

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
   

  
