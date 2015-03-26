import random
from players import *
from constants import *



def listAll(node):
    all = [node]
    for child in node.children+node.removed_children:
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
  counts = {}
  for player in players:
    if not type(player) in sums:
      sums[type(player)] = 0
      counts[type(player)] = 0
    sums[type(player)]+=player.cookies + player.reward
    counts[type(player)]+=1

  for playertype in sums:
    cookies = sums[playertype]
    count = counts[playertype]
    avg = float(cookies)/count
    print("type %s avg: %4d sum:%d count:%d" %(playertype, avg, cookies, count))
      
root = simulate(200,[ Cooperator, Defector])
allPlayers = listAll(root)

allPlayers.sort(key=Player.score, reverse=True)
print("Players sorted by score")
for player in allPlayers:
    print("%20s %5d %5d %5d"%(player, player.score(), player.cookies, player.reward))          

printSumByType(allPlayers)

