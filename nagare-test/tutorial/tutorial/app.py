from nagare import component, presentation
from counter import Counter
from nagare.namespaces import xhtml

class App(object):

    def __init__(self):
        self.counter1 = component.Component(Counter(), model="freezed")
        self.counter2 = component.Component(Counter())


@presentation.render_for(App)
def render(self, h, *args):
    h << self.counter1.render(xhtml.AsyncRenderer(h))
    h << h.hr
    h << self.counter2

    return h.root

# factory
app = App

