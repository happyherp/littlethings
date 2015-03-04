import random
from players import *
from constants import *



def listAll(root):
    all = [root]
    for child in root.children:
      all += listAll(child)
    return all

def simulate(startmoney, playersources):

    root = random.choice(playersources)(startmoney, playersources)
    
    while root.isPlaying:
        action = root.pickAction()
        action.execute()
        
        
    return root

def printSumByType(players):
  sums = {}
  for player in players:
    if not type(player) in sums:
      sums[type(player)] = 0
    sums[type(player)]+=player.cookies + player.reward

  print(sums)    
      
root = simulate(100,[Altruist, Defector])
allPlayers = listAll(root)
for player in allPlayers:
    print(type(player), player.cookies, player.reward)          

printSumByType(allPlayers)

