from history import HistoryPlayer
from game import START_ROUND
from utils import *
from random import shuffle

class Repval(HistoryPlayer):

  def __init__(self):
    HistoryPlayer.__init__(self)
    self.hunt_ratio_history = []
  
  def makeChoice(self, round_number):

    
    hunt_ratio = self.chooseHuntRatio(round_number)
    self.hunt_ratio_history.insert(0, hunt_ratio)

    #with anyone, as long as ration is preserved
    choices = []
    for i in range(int(len(self.curRep())*hunt_ratio)):
      choices.append("h")

    while len(choices) < len(self.curRep()):
      choices.append("s")

    shuffle(choices)
    return choices


    reps = self.curRep()[:]
    cutoff = reps[int((len(reps)-1)*hunt_ratio)]

    results = []
    for rep in self.curRep():
      if rep > cutoff: 
        results.append('h')
      else:
        results.append('s')

    return results


  def chooseHuntRatio(self, round_number):
    
    if round_number == START_ROUND:
      return 0.2
    elif round_number == START_ROUND+1:
      return 0.1
    else:

      diff = self.hunt_ratio_history[0] - self.hunt_ratio_history[1] 

      if self.lastRoundWasBetter():
        return self.hunt_ratio_history[0] + diff * 0.9
      else:
        return self.hunt_ratio_history[0] - diff * 0.9
        
  def lastRoundWasBetter(self):
    return  (countHunts(reconstructOthersChoices(self.food_earnings_history[0]))
             > countHunts(reconstructOthersChoices(self.food_earnings_history[1])))
  
  def __repr__(self):
    s = HistoryPlayer.__repr__(self)
    if len(self.hunt_ratio_history) > 0:
      s += " ratio:%.2f" %(self.hunt_ratio_history[0] )
    return s
      
      
class RepvalFull(Repval):
  
  def chooseHuntRatio(self, round_number):
    
    if round_number == START_ROUND:
      return 1.0
    elif round_number == START_ROUND+1:
      return 0.9
    else:

      diff = self.hunt_ratio_history[0] - self.hunt_ratio_history[1] 

      if self.lastRoundWasBetter():
        if diff > 0:
          return 1.0
        else:
          return 0.0
      else:
        if diff < 0:
          return 1.0
        else:
          return 0.0
        
      
