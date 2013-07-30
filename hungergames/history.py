
from simple import BasePlayer

class HistoryPlayer(BasePlayer):

  def __init__(self):
    self.reputation_history = []
    self.food_history = []
    self.other_reputation_history = []
    self.food_earnings_history = []

  def hunt_choices(self, round_number, current_food, current_reputation, m,player_reputations):
    self.food_history.insert(0,current_food)
    self.reputation_history.insert(0,current_reputation)
    self.other_reputation_history.insert(0,player_reputations)
    return self.makeChoice(round_number)

  def curRep(self):
    return self.other_reputation_history[0]

  def makeChoice(self):
    '''Subclasses can implement their algorithm here'''
    pass
  
  def hunt_outcomes(self, food_earnings):
    self.food_earnings_history.insert(0,food_earnings)
