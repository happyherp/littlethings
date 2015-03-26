from constants import *
from actions import *

class Player:

    count = 1

    def __init__(self, money, playersources):
        self.id = Player.count    
        Player.count += 1
        self.cookies = 0
        self.money = money
        self.startmoney = money
        self.playersources = playersources
        self.actions = []
        self.children = []
        self.removed_children = []
        self.reward = 0
        self.rewardMax = self.money * REWARD_MULTIPLIER
        self.isPlaying = True
        print(self, "created with money", money)
     
    def canReceiveReward(self):
        return self.reward < self.rewardMax
        
    def __str__(self):
        return "<%s::%d> "%(self.__class__.__name__, self.id)
        
    def dispose(self, parent):
      for child in self.children:
         child.dispose(self)    
    
      parent.children.remove(self)
      parent.removed_children.append(self)
      print("money reclaimed", int(self.money / TRANSFER_COST_FACTOR))
      parent.money += int(self.money / TRANSFER_COST_FACTOR)
      self.isPlaying = False
      self.money=0
      self.reward=0

      
    def score(self):
      return self.cookies + self.reward
        
    def interceptAction(self, action):
       pass
        
class Defector(Player):
    '''Just eats all the cookies'''
    
    def pickAction(self):
        if self.money > 0:
          return Eat(self)
        else:  
          return Quit(self)
                    

class Altruist(Player):  
    '''Creates a Child and then supports it. Unconditional love.'''

          
    def pickAction(self):
        if len(self.children) == 0 and self.money >= CREATE_CHILD_COST:
            return CreateChild(self, self.money-1)
        elif len(self.children) == 1 and self.children[0].canReceiveReward():
            return GiveReward(self, self.children[0])    
        elif len(self.children) == 1 and self.children[0].isPlaying:
            return WaitForChild(self, self.children[0])
        else:
          return Quit(self)

TIME_TO_COOPERATE = 0.5
COOPERATION_REQ_MONEY = 5
      
class Cooperator(Player):
    '''Cooperates with child if it cooperates with its childs.'''

    
    def pickAction(self):
        if len(self.children) == 0 and  self.money > CREATE_CHILD_COST:
            return CreateChild(self, self.money-1)
        elif len(self.children) == 1:
            child = self.children[0]
            if float(child.money/child.startmoney) < TIME_TO_COOPERATE:
                if self.didChildCooperate(child):
                    if child.canReceiveReward():
                        return GiveReward(self, child)
                    elif child.isPlaying:
                        return WaitForChild(self, child)
                    else:
                        return Quit(self)
                elif type(child.actions[-1]) == Eat or not child.isPlaying:
                    return Reclaim(self, child)
                else: 
                    return WaitForChild(self, child)
            else:
                return WaitForChild(self, child)
        
        if self.money > 0:
            return Eat(self)
        else:        
            return Quit(self)
       
    def didChildCooperate(self, child):
        print("didChildCooperate")
        if child.startmoney < COOPERATION_REQ_MONEY:
          return True
    
        #For now someone is not cooperating if he is eating late in the game.
        if type(child.actions[-1]) == Eat:
            return False
        return True
          