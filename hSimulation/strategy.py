
from constants import *
from actions import *

class Strategy(object):

    def pickAction(self, player):
       pass

def strategySourceFromList(strategies):
    '''Creates a playersource that returns the given players. The last one will be used repeatedly'''
    
    def source(*args):
       nonlocal strategies
       strategy = strategies[0](*args)
       if len(strategies) > 1:
         del strategies[0]
       return strategy
    return source
    
    
    
    
def weightedChoice(strategies):
    '''Takes a list of (relative_probability, strategy) and creates a 
    strategySource that will create each strategy with the given probability'''

class Defector(Strategy):
    '''Just eats all the cookies'''
    
    def pickAction(self, player):
        if player.money > 0:
          return Eat(player)
        else:  
          return Quit(player)
                    

class Altruist(Strategy):  
    '''Creates a Child and then supports it. Unconditional love.'''

          
    def pickAction(self, player):
        if len(player.children) == 0 and player.money >= CREATE_CHILD_COST:
            return CreateChild(player, player.money-1)
        elif len(player.children) == 1 and player.children[0].canReceiveReward():
            return GiveReward(player, player.children[0])    
        elif len(player.children) == 1 and player.children[0].isPlaying:
            return WaitForChild(player, player.children[0])
        else:
          return Quit(player)

TIME_TO_COOPERATE = 0.5
COOPERATION_REQ_MONEY = 5
      
class Cooperator(Strategy):
    '''Cooperates with child if it cooperates with its childs.'''

    
    def pickAction(self, player):
        if len(player.children) == 0 and  player.money > CREATE_CHILD_COST:
            return CreateChild(player, player.money-1)
        elif len(player.children) == 1:
            child = player.children[0]
            if float(child.money/child.startmoney) < TIME_TO_COOPERATE:
                if self.didChildCooperate(player, child):
                    if child.canReceiveReward():
                        return GiveReward(player, child)
                    elif child.isPlaying:
                        return WaitForChild(player, child)
                    else:
                        return Quit(player)
                elif type(child.actions[-1]) == Eat or not child.isPlaying:
                    return Reclaim(player, child)
                else: 
                    return WaitForChild(player, child)
            else:
                return WaitForChild(player, child)
        
        if player.money > 0:
            return Eat(player)
        else:        
            return Quit(player)
       
    def didChildCooperate(self, player, child):
        print("didChildCooperate")
        if child.startmoney < COOPERATION_REQ_MONEY:
          return True
    
        #For now someone is not cooperating if he is eating late in the game.
        if type(child.actions[-1]) == Eat:
            return False
        return True
          
          
class SelfCooperator(Cooperator):
    
    def didChildCooperate(self, player, child):
        #Pretend we where in position of child. 
        # If the child does something different than we would, do not cooperates        
        
        myaction = self.pickAction(child)
        return type(myaction) == type(child.actions[-1])
    
    
    
#Separate Player and strategy.     