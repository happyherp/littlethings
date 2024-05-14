
from history import HistoryPlayer
from utils import *
from game import START_ROUND
from random import random

class TitForTatPlayer(HistoryPlayer):

  def __init__(self, start_ratio=0.6):
    self.start_ratio = start_ratio
    HistoryPlayer.__init__(self)
  
  def makeChoice(self, round_number):

    choices = []     
    
    for rep in self.other_reputation_history[0]:

      #initialize reprociperation occasionly
      #if random() < 
      


      trackedTo = self.backTrack(rep)
      if round_number > START_ROUND:
        others_choices_last_round = self.getChoicesInLastRoundForRep(self.backTrack(rep))
        appendHuntIf(choices, random() < huntRatio(others_choices_last_round))
      else:
        appendHuntIf(choices, random() < self.start_ratio)


    return choices

  def getChoicesInLastRoundForRep(self,reps):
    '''Returns the choices that players with the given reps made to me last round'''
    choices = []


    for i in range(len(self.other_reputation_history[1])):
      rep_i = self.other_reputation_history[1][i]
      if rep_i in reps:
        choices.append(reconstructOthersChoices(self.food_earnings_history[0])[i])
    return choices
        
