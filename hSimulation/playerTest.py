from players import *
from actions import *
import unittest

class PlayerTest(unittest.TestCase):

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

    def test_CooperatorDefect(self):
        
        coop = Cooperator(20, [Defector])
        
        action=coop.pickAction()
        self.assertEqual(type(action), CreateChild)
        action.execute()

        action=coop.pickAction()
        self.assertEqual(type(action), WaitForChild)
        action.execute()
        
        action=coop.pickAction()
        while type(action) == WaitForChild:
            action.execute()
            action=coop.pickAction()

        self.assertEqual(type(action), Reclaim)
        action.execute()
        

    def test_CooperatorCooperate(self):
        
        coop = Cooperator(20, [Altruist])
        
        action=coop.pickAction()
        self.assertEqual(type(action), CreateChild)
        action.execute()

        action=coop.pickAction()
        while type(action) == WaitForChild:
            action.execute()
            action=coop.pickAction()

        self.assertEqual(type(action), GiveReward) 
        action.execute()


    def test_Retry(self):
    
        once = True
        def playersource(*args):
            nonlocal once
            if once:
               once = False
               return Defector(*args)
            else:
               return Altruist(*args)
    
    
        coop = Cooperator(40, [playersource])
        
        action=coop.pickAction()
        self.assertEqual(type(action), CreateChild)
        action.execute()

        action=coop.pickAction()
        self.assertEqual(type(action), WaitForChild)
        action.execute()
        
        action=coop.pickAction()
        while type(action) == WaitForChild:
            action.execute()
            action=coop.pickAction()

        self.assertEqual(type(action), Reclaim) 
        action.execute()

        action=coop.pickAction()
        self.assertEqual(type(action), CreateChild)
        action.execute()

        action=coop.pickAction()
        while type(action) == WaitForChild:
            action.execute()
            action=coop.pickAction()

        self.assertEqual(type(action), GiveReward) 
        action.execute()
        
        
if __name__ == "__main__":
    unittest.main()