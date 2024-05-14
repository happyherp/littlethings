
from simple import BasePlayer
from game import START_ROUND

class HistoryPlayer(BasePlayer):

  def __init__(self):
    self.reputation_history = []
    self.food_history = []
    self.other_reputation_history = []
    self.food_earnings_history = []
    self.decision_history = []

  def hunt_choices(self, round_number, current_food, current_reputation, m,player_reputations):
    self.food_history.insert(0,current_food)
    self.reputation_history.insert(0,current_reputation)
    self.other_reputation_history.insert(0,player_reputations)
    decisions = self.makeChoice(round_number)
    self.decision_history.insert(0,decisions)
    return decisions

  def curRep(self):
    return self.other_reputation_history[0]

  def makeChoice(self):
    '''Subclasses can implement their algorithm here'''
    pass
  
  def hunt_outcomes(self, food_earnings):
    self.food_earnings_history.insert(0,food_earnings)

  def backTrack(self, rep):
    '''Finds the players(by Rep) in the last round that could have the given reputation 
     |rep| in this round.'''
    
    #the round we are in right now
    current_round = len(self.other_reputation_history)

    #round in which to track the player
    last_round = current_round-1

    #if there is no previous round, it could be everybody
    if last_round == START_ROUND -1:
      return self.other_reputation_history[0]
    
    #maximum change of reputation possible in given round.
    maxrepdiff = float(1)/last_round

    #return all players
    possibleplayers = filter( lambda r: abs(r-rep) <= maxrepdiff, 
                              self.other_reputation_history[1])

    return possibleplayers
     

