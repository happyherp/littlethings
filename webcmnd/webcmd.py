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

  def sendRead(self, foo):
    '''Sends the given response to the client, waits for the answer'''
    global flowcount, openflows

    flowid = flowcount
    openflows[flowid] = self
    flowcount += 1

    self.channel.send(foo(flowid))
    request = self.channel.receive()
    del openflows[flowid]
    return request

  def inForm(self, flowid, content):
    '''Wraps the passed html string into form tags with the action pointing to followFlow'''
    return '<form action="/followFlow/'+str(flowid)+'">'+content +'</form>'

  def confirm(self, message):
    '''Lets the user anser a yes/no question'''
    def render(flowid):
      return Response(self.inForm(flowid, message
                                          + '<input type="submit" name="y" value="yes" />'
                                          + '<input type="submit" name="n" value="no" />'))
    request = self.sendRead(render)

    if request.GET.has_key("y"):
      return True
    elif request.GET.has_key("n"):
      return False
    else:
      raise Exception("Unexpected parameter in resonse to confirm")
 
  def input(self, message="", label="enter"):
    return self.sendRead(
      lambda flowid: Response(self.inForm(flowid, message 
                                         +'<input type="text" name="textinput" />'
                                         +'<input type="submit" value="'+label+'" />')
                             )).GET["textinput"]  

  def showMessage(self, message):
    self.sendRead(lambda flowid: Response(self.inForm(flowid, message 
                                         +'<input type="submit" value="OK" />')
                             ))
    return None

  def select(self, options, message = "Waehle beliebig aus den folgenden"):
    html = message + "<br />"
    i=0
    for option in options:
      html += '<input type="checkbox" name="gencheckbox" value="%d">%s<br>'%(i,str(option))
      i += 1
    html += '<input type="submit" value="OK" />'

    response = self.sendRead(lambda flowid: Response(self.inForm(flowid,html)))  
    selected = []
    for index_s in response.GET.getall("gencheckbox"):
       selected.append(options[int(index_s)])
    return selected

def startFlow(request, flow):
  print "startFlow"

  #start new microthread containing the flow.
  def flowRunner():
    lastresponse = flow.run(request)
    flow.channel.send(lastresponse)
  stackless.tasklet(flowRunner)()
  print "startflow tasklet online"
  firstresponse = flow.channel.receive()
  print "startFlow END"
  return firstresponse


def followFlow(request):
  print "followFlow", openflows
  flowid = int(request.matchdict['id'])
  flow = openflows[flowid]
  flow.channel.send(request)
  response = flow.channel.receive()
  return response
   

  
