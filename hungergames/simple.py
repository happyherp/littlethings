from random import random
from game import START_ROUND

class BasePlayer(object):
  
    def hunt_outcomes(self, food_earnings):
        pass # do nothing

    def round_end(self, award, m, number_hunters):
        pass # do nothing

    def __repr__(self):
      return self.__class__.__name__


class Defector(BasePlayer):

    def hunt_choices(self, round_number, current_food, current_reputation, m,
            player_reputations):
        hunt_decisions = ['s' for x in player_reputations]
        return hunt_decisions

class Hunter(BasePlayer):

    def hunt_choices(self, round_number, current_food, current_reputation, m,
            player_reputations):
        hunt_decisions = ['h' for x in player_reputations]
        return hunt_decisions


class Threeshold(BasePlayer):

  def __init__(self, threeshold = 0.7):
    self.threeshold = threeshold

  def hunt_choices(self, round_number, current_food, current_reputation, m,
          player_reputations):
      hunt_decisions = []

      for rep in player_reputations:
        if round_number == START_ROUND or rep > self.threeshold: 
          c = "h" 
        else:
          c = "s" 
        hunt_decisions.append(c)
      return hunt_decisions

  def __repr__(self):
    return "Threeshold:%f"%(self.threeshold) 

class RandomPlayer(BasePlayer):
  
  def __init__(self, chance=0.2):
    self.chance = chance  

  def hunt_choices(self, round_number, current_food, current_reputation, m,
          player_reputations):
      hunt_decisions = []

      for rep in player_reputations:
        if random() <= self.chance: 
          c = "h" 
        else:
          c = "s" 
        hunt_decisions.append(c)        
      return hunt_decisions


class Reputist(BasePlayer):
  '''Only shared with those that have a reputation close to his own'''
  
  def __init__(self, startrep = 0.95):
    self.startrep = startrep


  def hunt_choices(self, round_number, current_food, current_reputation, m,
          player_reputations):
      hunt_decisions = []

      if round_number == START_ROUND:
        for rep in player_reputations:
          if random() <= self.startrep: 
            c = "h" 
          else:
            c = "s" 
          hunt_decisions.append(c)        
      else:
        
        #figure out the X% of players with reputation closest to mine. 
        def relDiff(r):
          return abs(r-current_reputation)
        related = player_reputations[:]
        related.sort(key=relDiff)
        related = related[:len(related)/10]

        for rep in player_reputations:
          if rep in related: 
            c = "h" 
          else:
            c = "s" 
          hunt_decisions.append(c)
      return hunt_decisions

class GoodGoneBad(BasePlayer):

  def hunt_choices(self, round_number, current_food, current_reputation, m,
          player_reputations):
      hunt_decisions = []

      for rep in player_reputations:

        if round_number == START_ROUND:
          c = "h" 
        else:
          c = "s" 
        hunt_decisions.append(c)        

      return hunt_decisions


