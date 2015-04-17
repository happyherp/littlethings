from players import *
from actions import *
import unittest



def makePlayerSource(players):
    '''Creates a playersource that returns the given players. The last one will be used repeatedly'''
    
    def source(*args):
       nonlocal players
       player = players[0](*args)
       if len(players) > 1:
         del players[0]
       return player
    return source


class PlayerTest(unittest.TestCase):
        
    def waitForChild(self, player):
        '''Lets the player perform wait for child until it does something else'''
        action=player.pickAction()
        while type(action) == WaitForChild:
            action.execute()
            action=player.pickAction()    
        return action
        
    def assertNextAction(self, player, actiontype):
        action = player.pickAction()
        self.assertAction(action, actiontype)
        
    def assertAction(self, action, actiontype):
        self.assertEqual(type(action), actiontype)
        action.execute()        


class AltruistTest(PlayerTest):

    def testReward(self):
    
        p1 = Altruist(10, [])
        p2 = Altruist(10, [])
        self.assertTrue(p2.canReceiveReward())
        
        action = GiveReward(p1,p2)
        action.execute()
        self.assertFalse(p2.canReceiveReward())

        try:
            action = GiveReward(p1,p2)
            action.execute()
            self.fail()
        except:
            pass

            
class CooperatorTest(PlayerTest):
            
            
    def test_CooperatorDefect(self):        
        coop = Cooperator(20, [Defector])               
        self.assertNextAction(coop, CreateChild)
        self.assertNextAction(coop, WaitForChild)               
        action=self.waitForChild(coop)
        self.assertAction(action, Reclaim)
        

    def test_CooperatorCooperate(self):        
        coop = Cooperator(20, [Altruist])        
        self.assertNextAction(coop, CreateChild)
        action=self.waitForChild(coop)
        self.assertAction(action, GiveReward)

    def test_RetryAfterDefector(self):
    
        playersource = makePlayerSource([Defector, Altruist])        
        coop = Cooperator(40, [playersource])   
        #Reclaim the defector
        self.assertNextAction(coop, CreateChild)
        self.assertNextAction(coop, WaitForChild)                       
        action=self.waitForChild(coop)
        self.assertAction(action, Reclaim)
        #Reward the altruist
        self.assertNextAction(coop, CreateChild)
        action=self.waitForChild(coop)
        self.assertAction(action, GiveReward)

        
        
class SelfCooperatorTest(PlayerTest):

    def testDefectToAltruist(self):
        playersource = makePlayerSource([Altruist])        
        player = SelfCooperator(40, [playersource])   
        self.assertNextAction(player, CreateChild)
        action=self.waitForChild(player)                       
        self.assertAction(action, Reclaim)

    def testDefectToDefector(self):
        playersource = makePlayerSource([Defector])        
        player = SelfCooperator(40, [playersource])   
        self.assertNextAction(player, CreateChild)
        action=self.waitForChild(player)                       
        self.assertAction(action, Reclaim)        
        
if __name__ == "__main__":
    unittest.main()