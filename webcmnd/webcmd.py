from pyramid.response import Response

import stackless


#TODO: move those into some kind of container. global state is bad.
flowcount = 0 
openflows = {}

class Flow:
  '''Superclass for all Flows. subclasses must implement a run method. 
  Currently the only state for the class is the channel. 
  ''' 

  def __init__(self):
    self.channel = stackless.channel()
    self.flowid = None
    self.step = 1 #Counts how many steps we have gone in this flow.
    self.prevResponse = None #The last response send to the client.

  def sendRead(self, response):
    '''Sends the response that is returned by foo to the client, waits for the answer'''

    self.prevResponse = response
    self.channel.send(response)
    request = self.channel.receive()
    self.step += 1

    return request

  def inForm(self, content):
    '''Wraps the passed html string into form tags with the action pointing to followFlow'''
    return '<form action="/followFlow/%d/%d">%s</form>' %(self.flowid, self.step, content)

  def confirm(self, message):
    '''Lets the user answer a yes/no question'''
    response = Response(self.inForm(message
                                          + '<input type="submit" name="y" value="yes" />'
                                          + '<input type="submit" name="n" value="no" />'))
    request = self.sendRead(response)

    if request.GET.has_key("y"):
      return True
    elif request.GET.has_key("n"):
      return False
    else:
      raise Exception("Unexpected parameter in response to confirm")
 
  def input(self, message="", label="enter"):
    return self.sendRead(Response(self.inForm(message 
                                              +'<input type="text" name="textinput" />'
                                              +'<input type="submit" value="'+label+'" />')
                        )).GET["textinput"]  

  def input_number(self, message):
    result = self.input(message)
    number = None
    while number == None:
      try:
        number = int(result)
      except:
        result = self.input("Please try a number. "+ message)
    return number

  def showMessage(self, message):
    self.sendRead(Response(self.inForm(message 
                                       +'<input type="submit" value="OK" />')))
    return None

  def select(self, options, message = "Waehle beliebig aus den folgenden"):
    html = message + "<br />"
    i=0
    for option in options:
      html += '<input type="checkbox" name="gencheckbox" value="%d">%s<br>'%(i,str(option))
      i += 1
    html += '<input type="submit" value="OK" />'

    response = self.sendRead(Response(self.inForm(html)))  
    selected = []
    for index_s in response.GET.getall("gencheckbox"):
       selected.append(options[int(index_s)])
    return selected

  def doForm(self, elements):
    inner = ""
    i = 0
    for element in elements:
      element.name = "elementname"+str(i)
      inner += element.render()
      i += 1

    request = self.sendRead(Response(self.inForm(inner)))  

    result = []
    for element in elements:
      result.append(element.extractValue(request))
    return result

def startFlow(request, flow):
  print "startFlow"

  global flowcount
  #register the new flow.
  flowid = flowcount
  flow.flowid = flowid
  openflows[flowid] = flow
  flowcount += 1

  #start new microthread containing the flow.
  def flowRunner():
    lastresponse = flow.run(request)
    del openflows[flowid]    
    flow.channel.send(lastresponse)
  stackless.tasklet(flowRunner)()

  print "startflow tasklet online"
  stackless.run()
  firstresponse = flow.channel.receive()
  print "startFlow END"
  return firstresponse


def followFlow(request):
  print "followFlow", openflows
  flowid = int(request.matchdict['id'])
  step = int(request.matchdict['step'])

  if openflows.has_key(flowid):
    flow = openflows[flowid]
    if step == flow.step:
      #If it is the current step, get new response
      flow.channel.send(request)
      stackless.run()
      response = flow.channel.receive()
    else:
      #Old step(user hit enter in url-bar). Return the last response we send, 
      #so he can continue.
      response = flow.prevResponse
  else:
    response = Response("Flow not found.") 
  return response
   

  
