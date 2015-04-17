import random
from player import Player
from constants import *
from strategy import *



def listAll(node):
    all = [node]
    for child in node.children+node.removed_children:
      all += listAll(child)
    return all
    
def simulate(startmoney, strategysource, rounds)

  sums = {}
  for round in range(rounds):
    root = simulateRound(startmoney, strategysource)
    thisRoundSums = makeSums(listAll(root))
    addSums(sums, thisRoundSums)
    
  
def addSums(target, addition):
    for key in addition:
      if not key in target:
         target[key] = (0,0)
      sum1, count1 = target[key]
      sum2, count2 = addition[key]
      target[key] = (sum1+sum2, count1, count2)
  
def simulateRound(startmoney, strategysource):

    root = Player(startmoney,strategysource(),strategysource)
    
    while root.isPlaying:
        action = root.pickAction()
        action.execute()               
    return root
    
def makeSums(players):
  sums = {}
  counts = {}
  for player in players:
    strategytype = type(player.strategy)
    if not strategytype in sums:
      sums[strategytype] = 0
      counts[strategytype] = 0
    sums[strategytype]+=player.cookies + player.reward
    counts[strategytype]+=1
  return sums, counts

def printSums(sums):

  for strategytype in sums:
    cookies = sums[strategytype]
    count = counts[strategytype]
    avg = float(cookies)/count
    print("type %s avg: %4d sum:%d count:%d" %(strategytype, avg, cookies, count))
      
root = simulateRound(200,[ Cooperator, Defector, Altruist])
allPlayers = listAll(root)

allPlayers.sort(key=Player.score, reverse=True)
print("Players sorted by score")
for player in allPlayers:
    print("%20s %5d %5d %5d"%(player, player.score(), player.cookies, player.reward))          

sums, counts = makeSums(allPlayers)
printSumByType(sums, counts)

