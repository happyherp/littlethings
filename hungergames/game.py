from random import randint

class Player(object):
  def __init__(self, food, algorithm):
    self.food = food
    self.hunts = 0
    self.defects = 0
    self.algorithm = algorithm

  def getRep(self):
    if self.hunts+self.defects == 0:
      return 0
    else:
      return float(self.hunts) / (self.hunts+self.defects) 

  def __repr__(self):
    return self.algorithm.__repr__()+" with food "+ str(self.food)

def playGame(algorithms, n = 1000):

  startfood = 300* (len(algorithms)-1)
  players = [Player(startfood, alg) for alg in algorithms]
  
  turn = 0
  while turn != n and len(filter(lambda p: p.food > 0, players)) != 1:
    
    runRound(turn, players)
    turn += 1

  return max(players, key = lambda p: p.food).algorithm

def printPlayers(players):
  i = 0
  for player in players:
    print i, player
    i += 1

def printResults(players, results):

  print " ",
  for y in range(len(players)):
    print y,
  print

  x = 0
  for xplayer in players:
    print x,
    for yplayer in players:
      if xplayer != yplayer:
        print results[xplayer][yplayer],
      else: print "X",
    print
    x += 1


def runRound(turn, players):

  #remove dead players:
  players = filter(lambda p: p.food > 0, players)
  if [] == players: return

  m = randint(0,len(players)*(len(players)-1))

  coopcount = 0

  
  print "round", turn
  printPlayers(players)
  
  allresults = {}
  #Let all players hunt with each other
  for player in players:
    other_players = players[:]
    other_players.remove(player)
    reputations = map( lambda p: p.getRep(), other_players) 
    result = player.algorithm.hunt_choices(turn, player.food, 
                                           player.getRep(), m, reputations)
    #save result such that allresults[a][b] = "h" means that a hunted with b
    allresults[player] = dict(zip(other_players, result)) 

  printResults(players, allresults)


  for player in players:
    other_players = players[:]
    other_players.remove(player)
    earnings = []
    for other in other_players:
      #calculate earnings
      earn = hunt(allresults[player][other],
                  allresults[other][player])
      player.food += earn
      earnings.append(earn)
      player.algorithm.hunt_outcomes(earnings)

      #count h/s
      if allresults[player][other] == "h":
        player.hunts += 1
        coopcount += 1
      else:
        player.defects += 1

    
  if coopcount >= m:
    extrafood = 2*(len(players)-1)
    print "extra food is given out", extrafood
  else:
    extrafood = 0

  for player in players:
    player.algorithm.round_end(extrafood, m, coopcount)
     
  
# calculates the food earnings for the first player
def hunt(a,b):
  if a == "h" and  b == "h":
    return 0
  elif a == "h" and b == "s":
    return -3
  elif a == "s" and b == "h":
    return 1
  elif a == "s" and b == "s":
    return -2
  else:
    raise "invalid hunt parameters"
      
if __name__ == "__main__":
  from simple import *
  playeralgs = [Defector(), Hunter(), Hunter(), Threeshold(), Threeshold(0.3), Threeshold(0.9), CheckRep()]
  #playeralgs = [Defector(), Threeshold(0.4), Threeshold(0.4)]
  print playGame(playeralgs).__class__

  
