
class Defector(object):

    def hunt_choices(self, round_number, current_food, current_reputation, m,
            player_reputations):
        hunt_decisions = ['s' for x in player_reputations]
        return hunt_decisions

    def hunt_outcomes(self, food_earnings):
        pass # do nothing

    def round_end(self, award, m, number_hunters):
        pass # do nothing

    def __repr__(self):
      return "Defector"

class Hunter(object):

    def hunt_choices(self, round_number, current_food, current_reputation, m,
            player_reputations):
        hunt_decisions = ['h' for x in player_reputations]
        return hunt_decisions

    def hunt_outcomes(self, food_earnings):
        pass # do nothing

    def round_end(self, award, m, number_hunters):
        pass # do nothing

    def __repr__(self):
      return "Hunter"

class Threeshold(object):

  def __init__(self, threeshold = 0.7):
    self.threeshold = threeshold

  def hunt_choices(self, round_number, current_food, current_reputation, m,
          player_reputations):
      hunt_decisions = []
      for rep in player_reputations:
        if round_number == 0 or rep > self.threeshold: 
          c = "h" 
        else:
          c = "s" 
        hunt_decisions.append(c)
      return hunt_decisions

  def hunt_outcomes(self, food_earnings):
      pass # do nothing

  def round_end(self, award, m, number_hunters):
      pass # do nothing

  def __repr__(self):
    return "Threeshold:%f"%(self.threeshold) 


class CheckRep(object):
  '''Considers the Reputation of the other player to make best decisison'''  

  def hunt_choices(self, round_number, current_food, current_reputation, m,
          player_reputations):
      hunt_decisions = []
      for rep in player_reputations:
        if round_number == 0 : 
          c = "s" 
        else: 
          ps = 1.0-rep #probability other player will slack
          if ps *-3 >=  ps * -2 + rep:
            c = "h"
          else:
            c = "s"
        hunt_decisions.append(c)
      return hunt_decisions



  def hunt_outcomes(self, food_earnings):
      pass # do nothing

  def round_end(self, award, m, number_hunters):
      pass # do nothing

  def __repr__(self):
    return "CheckRep"
 
