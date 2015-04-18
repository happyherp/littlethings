import random
from player import Player
from constants import *
from strategy import *



def listAll(node):
    all = [node]
    for child in node.children+node.removed_children:
      all += listAll(child)
    return all
    
def simulate(startmoney, strategysource, rounds):

  summary = ({},{})
  for round in range(rounds):
    root = simulateRound(startmoney, strategysource)
    sums, counts = makeSums(listAll(root))
    addDicts(summary[0], sums)
    addDicts(summary[1], counts)
        
  return summary  
  
def addDicts(target, addition):
    for key in addition:
      if not key in target:
         target[key] = 0
      target[key] += addition[key]
 
  
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

def printSums(sums, counts):
  print("Performance of strategies")
  for strategytype in sums:
    cookies = sums[strategytype]
    count = counts[strategytype]
    avg = float(cookies)/count
    print("type %20s avg: %4d sum:%4d count:%3d" %(strategytype.__name__, avg, cookies, count))
   

def printScore(players):
    allPlayers.sort(key=Player.score, reverse=True)
    print("Players sorted by score      | score-cookies-reward")
    for player in allPlayers:
        print("%30s %5d %5d %5d"%(player, player.score(), player.cookies, player.reward))          
       
       
strategieprobs = [
  (0.5, Cooperator),
  (0.5, Altruist),
  (0.5, Defector),
  (0.5, SelfCooperator)
]
  


def runSingleRound():
  
    root = simulateRound(200, weightedChoice(strategieprobs))
    allPlayers = listAll(root)


    sums, counts = makeSums(allPlayers)
    print(allPlayers)
    printScore(allPlayers)
    printSums(sums, counts)

def runMultipleRounds():
    sums, counts = simulate(200, weightedChoice(strategieprobs), 1000)
    printSums(sums, counts)
    
    
runMultipleRounds()    