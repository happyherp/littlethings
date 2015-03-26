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
      print(self.source, "eats")
      if self.source.money > 0:
        self.source.money-=1
        self.source.cookies+=1
      else:
        raise Exception("Tried to eat without money")

class Quit(Action):
    
    def doAction(self):
        print(self.source, "quits")
        self.source.isPlaying = False
    
class CreateChild(Action):

    def __init__(self, source, startmoney):
        Action.__init__(self, source)
        self.startmoney = startmoney
        
    def doAction(self):
       print(self.source, "creates child")
       if self.source.money < self.startmoney+CREATE_CHILD_COST:
         raise Exception("Do not have enough money to create child")
       else:
         self.source.money -= self.startmoney + CREATE_CHILD_COST
         child = random.choice(self.source.playersources
                               )(self.startmoney * TRANSFER_COST_FACTOR, self.source.playersources)
         self.source.children.append(child)
         
    
class GiveReward(Action):

    def __init__(self, source, target):
      Action.__init__(self, source)
      self.target = target
      
    def doAction(self):
      print(self.source, "gives reward to ", self.target)
      if self.target.canReceiveReward():
        self.target.reward = self.target.rewardMax
      else:
        raise Exception("Can not give any more rewards")
        
class WaitForChild(Action):
    def __init__(self, source, child):
      Action.__init__(self, source)
      self.child = child
      
    def doAction(self):
      print(self.source, "waits for", self.child)
      if self.child.isPlaying:
        action = self.child.pickAction()
        action.execute()
      else:
        raise "Waiting for Player that is not playing anymore"
        
        
class Reclaim(Action):
    def __init__(self, source, child):
      Action.__init__(self, source)
      self.child = child
      
    def doAction(self):
      print(self.source, "reclaims", self.child)    
      self.child.dispose(self.source)
                 