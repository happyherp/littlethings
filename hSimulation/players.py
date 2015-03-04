from constants import *
from actions import *

class Player:

    def __init__(self, money, playersources):
        self.cookies = 0
        self.money = money
        self.playersources = playersources
        self.actions = []
        self.children = []
        self.reward = 0
        self.rewardMax = self.money * 10
        self.isPlaying = True
     
    def canReceiveReward(self):
        return self.reward < self.rewardMax
        
class Defector(Player):

    def pickAction(self):
        if self.money > 0:
          return Eat(self)
        else:
          return Quit(self)
          
          

class Altruist(Player):          
          
     def pickAction(self):
        if len(self.children) == 0 and self.money >= CREATE_CHILD_COST:
            return CreateChild(self, self.money-1)
        elif len(self.children) == 1 and self.children[0].canReceiveReward():
            return GiveReward(self, self.children[0])    
        elif len(self.children) == 1 and self.children[0].isPlaying:
            return WaitForChild(self, self.children[0])
        else:
          return Quit(self)
