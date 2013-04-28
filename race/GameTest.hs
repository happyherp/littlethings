import Test.HUnit

import Game
import IOUtils

field = ([((10,10),(20,20),40), ((50,19),(60,20),40)],[((30,30),(40,40),100)]) :: Gamefield

reachTest = TestCase (assertEqual "reach" 
                                     (((4,1),(1,0),7),([],[]),Won 8)
                                     (runRoute (strToRoute "EDD") 
                                               ([((1,1),(1,1),10),((3,1),(3,1),0)],[])))

noReachTest =  TestCase (assertEqual "noreach" 
                                     (((4,1),(1,0),7),([((5,1),(5,1),0)],[]),Driving)
                                     (runRoute (strToRoute "EDD") 
                                               ([((1,1),(1,1),10),((5,1),(5,1),0)],[])))

noGasTest = TestCase (assertEqual "nogas" 
                                     (((4,1),(1,0),-2),([],[]),NoGas)
                                     (runRoute (strToRoute "EDD") 
                                               ([((1,1),(1,1),1),((3,1),(3,1),0)],[])))



alltests = TestList [reachTest, noReachTest,noGasTest]
