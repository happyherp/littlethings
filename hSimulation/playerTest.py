from player import *
from actions import *
from strategy import *
import unittest




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
    
        p1 = Player(10, Altruist())
        p2 = Player(10, Altruist())
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
        coop = Player(20, Cooperator(), strategySourceFromList([Defector]))               
        self.assertNextAction(coop, CreateChild)
        self.assertNextAction(coop, WaitForChild)               
        action=self.waitForChild(coop)
        self.assertAction(action, Reclaim)
        

    def test_CooperatorCooperate(self):        
        coop = Player(20, Cooperator(), strategySourceFromList([Altruist]))               
        self.assertNextAction(coop, CreateChild)
        action = self.waitForChild(coop)
        self.assertAction(action, GiveReward)

    def test_RetryAfterDefector(self):
    
        coop = Player(40, Cooperator(), strategySourceFromList([Defector, Altruist]))  
        #Reclaim the defector
        self.assertNextAction(coop, CreateChild)
        self.assertNextAction(coop, WaitForChild)                       
        action = self.waitForChild(coop)
        self.assertAction(action, Reclaim)
        #Reward the altruist
        self.assertNextAction(coop, CreateChild)
        action = self.waitForChild(coop)
        self.assertAction(action, GiveReward)

        
        
class SelfCooperatorTest(PlayerTest):

    def testDefectToAltruist(self):
        player = Player(40, SelfCooperator(), strategySourceFromList([Altruist]))  
        self.assertNextAction(player, CreateChild)
        action = self.waitForChild(player)                       
        self.assertAction(action, Reclaim)

    def testDefectToDefector(self):
        player = Player(40, SelfCooperator(), strategySourceFromList([Defector]))  
        self.assertNextAction(player, CreateChild)
        action = self.waitForChild(player)                       
        self.assertAction(action, Reclaim)        
        
if __name__ == "__main__":
    unittest.main()