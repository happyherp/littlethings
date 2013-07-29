
from nagare import presentation

class Counter(object):

    def __init__(self):
        self.val = 0

    def increase(self):
        self.val += 1

    def decrease(self):
        self.val -= 1

@presentation.render_for(Counter)
def render(counter, h, comp, *args):
    h << h.div('Value: ', counter.val)

    h << h.a('++').action(counter.increase)
    h << '|'
    h << h.a('--').action(counter.decrease)

    h << h.br
    h << h.a('freeze').action(lambda: comp.becomes(counter, model='freezed'))

    return h.root

@presentation.render_for(Counter, model='freezed')
def render(counter, h, comp, *args):

    h << h.h1(counter.val)
    h << h.a('thaw').action(lambda: comp.becomes(counter)) 


    return h.root
app = Counter
