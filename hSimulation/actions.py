from constants import *
import random

class Action():
  def __init__(self, source):
    self.source = source
    
  def execute(self):
    self.source.actions.append(self)
    self.doAction()
          
class Eat(Action):

    def doAction(self):
      if self.source.money > 0:
        self.source.money-=1
        self.source.cookies+=1
      else:
        raise Exception("Tried to eat without money")

class Quit(Action):
    
    def doAction(self):
        self.source.isPlaying = False
    
class CreateChild(Action):

    def __init__(self, source, startmoney):
        Action.__init__(self, source)
        self.startmoney = startmoney
        
    def doAction(self):
       if self.source.money < self.startmoney+CREATE_CHILD_COST:
         raise Exception("Do not have enough money to create child")
       else:
         self.source.money -= self.startmoney + CREATE_CHILD_COST
         child = random.choice(self.source.playersources
                               )(self.startmoney, self.source.playersources)
         self.source.children.append(child)
         
    
class GiveReward(Action):

    def __init__(self, source, target):
      Action.__init__(self, source)
      self.target = target
      
    def doAction(self):
      if self.target.canReceiveReward():
        self.target.reward = self.target.rewardMax
      else:
        raise Exception("Can not give any more rewards")
        
class WaitForChild(Action):
    def __init__(self, source, child):
      Action.__init__(self, source)
      self.child = child
      
    def doAction(self):
      if self.child.isPlaying:
        action = self.child.pickAction()
        action.execute()
      else:
        raise "Waiting for Player that is not playing anymore"