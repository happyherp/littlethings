from constants import *
import random
from player import Player

class Action():
  def __init__(self, source):
    self.source = source
    #If this Action was a Response to another Action  here. 
    self.interruptedAction = None
    
  def execute(self):
  
    #check if the parent wants to intercept this action.
    parent = self.source.parent
    if parent:
        interception = parent.strategy.interruptChild(parent, self.source, self)
        if interception:
           if DEBUG: print(parent, "interrupted", self.source, "when doing", self)    
           interception.interruptedAction = self           
           interception.execute()
           return 
  
    self.source.actions.append(self)
    self.doAction()
          
class Eat(Action):

    def doAction(self):
      if DEBUG: print(self.source, "eats")
      if self.source.money > 0:
        self.source.money-=1
        self.source.cookies+=1
      else:
        raise Exception("Tried to eat without money")

class Quit(Action):
    
    def doAction(self):
        if DEBUG: print(self.source, "quits")
        self.source.isPlaying = False
    
class CreateChild(Action):

    def __init__(self, source, startmoney):
        Action.__init__(self, source)
        self.startmoney = startmoney
        
    def doAction(self):
       if DEBUG: print(self.source, "creates child")
       if self.source.money < self.startmoney+CREATE_CHILD_COST:
         raise Exception("Do not have enough money to create child")
       else:
         self.source.money -= self.startmoney + CREATE_CHILD_COST
         child = Player(self.startmoney * TRANSFER_COST_FACTOR,
                        self.source.strategysource(), 
                        self.source.strategysource,
                        self.source)
         self.source.children.append(child)
         
    
class GiveReward(Action):

    def __init__(self, source, target):
      Action.__init__(self, source)
      self.target = target
      
    def doAction(self):
      if DEBUG: print(self.source, "gives reward to ", self.target)
      if self.target.canReceiveReward():
        self.target.reward = self.target.rewardMax
      else:
        raise Exception("Can not give any more rewards")
        
class WaitForChild(Action):
    def __init__(self, source, child):
      Action.__init__(self, source)
      self.child = child
      
    def doAction(self):
      if DEBUG: print(self.source, "waits for", self.child)
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
      if DEBUG: print(self.source, "reclaims", self.child)    
      self.child.dispose()
                 