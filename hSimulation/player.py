from constants import *

class Player:

    count = 1

    def __init__(self, money, strategy, strategysource = None, parent = None):
        self.id = Player.count    
        Player.count += 1
        self.cookies = 0
        self.money = money
        self.strategy = strategy
        self.parent = parent
        self.startmoney = money
        self.strategysource = strategysource
        self.actions = []
        self.children = []
        self.removed_children = []
        self.reward = 0
        self.rewardMax = self.money * REWARD_MULTIPLIER
        self.isPlaying = True
        if DEBUG: print(self, "created with money", money)
     
    def canReceiveReward(self):
        return self.reward < self.rewardMax
        
    def __str__(self):
        return "<%s::%d> "%(self.strategy.__class__.__name__, self.id)
        
    def __repr__(self):
        return self.__str__()
        
    def dispose(self):
      for child in self.children:
         child.dispose()    
    
      self.parent.children.remove(self)
      self.parent.removed_children.append(self)
      if DEBUG: print("money reclaimed", int(self.money / TRANSFER_COST_FACTOR))
      self.parent.money += int(self.money / TRANSFER_COST_FACTOR)
      self.isPlaying = False
      self.money=0
      self.reward=0

      
    def score(self):
      return self.cookies + self.reward
      
    def pickAction(self):
        return self.strategy.pickAction(self)
        
        