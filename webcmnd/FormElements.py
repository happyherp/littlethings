# Contains functionality for Creating forms on a website, that know how to get their value.


class FormElement(object):
  '''self.name must be set before calling eiter render or extractValue'''

  def render(self):
    '''Return the html code of this element'''
    return "" 

  def extractValue(self, request):
    '''Return the value of this thing from the request. Returning None means 
    that this element is display only.'''
    return None


class Text(FormElement):

  def __init__(self, text):
    self.text = text;
  
  def render(self):
    return self.text+"<br>"


class Button(FormElement):

  def __init__(self, label="Enter"):
    self.label = label

  def render(self):
    return '<input type="submit" name="%s" value="%s" /><br>' %(self.name, self.label)

  def extractValue(self, request):
    return request.params.has_key(self.name)


class Select(FormElement):

  def __init__(self, options, labels=None):
    self.options = options
    if labels == None:
      labels = map(str, options)
    self.labels = labels

  def render(self):
    html = ""
    i=0
    for label in self.labels:
      html += '<input type="checkbox" name="%s" value="%d">%s<br>'%(self.name, i, label)
      i += 1
    return html

  def extractValue(self, request):
    selected = []
    for index_s in request.GET.getall(self.name):
       selected.append(self.options[int(index_s)])
    return selected

class Input(FormElement):

  def __init__(self, prefill=""):
    self.prefill = prefill

  def render(self):
    return '<input  type="text" name="%s" value="%s" /><br>'%(self.name, self.prefill)

  def extractValue(self, request):
    return request.params[self.name]
  
