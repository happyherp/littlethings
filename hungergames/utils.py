import sys

def printPlayers(players):
  i = 0
  for player in players:
    print i, player
    i += 1

def printResults(players, results):

  print "  ",
  for y in range(len(players)):
    sys.stdout.write( "%2d" %(y))
  print

  x = 0
  for xplayer in players:
    print "%2d" %(x),
    for yplayer in players:
      if xplayer != yplayer:
        print results[xplayer][yplayer],
      else: print "X",
    print
    x += 1

def countHunts(choices):
  i = 0
  for c in choices:
    if c == "h": i+= 1
  return i

def huntRatio(choices):
  return float(countHunts(choices))/len(choices)

def reconstructChoice(myearning):
  if myearning == 1 or myearning == 0:
    return "h"
  else:
    return "s"

def reconstructOthersChoices(myearnings):
  choices = []
  for myearning in myearnings:
    choices.append(reconstructChoice(myearning))
  return choices

def appendHuntIf(choices, doHunt):
  if doHunt:
    choices.append("h")
  else:
    choices.append("s")
