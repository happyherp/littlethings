import Test.HUnit

--A System is defined by a List of chars and their absolute values. Must be ordered by their absolute value.
type System = [(Char, Int)]

--Converts from relative Notation to absolute Notation
relToAbsSystem :: System -> System
relToAbsSystem [x] = [x]
-- Grab absolute value of previous Letter to calculate value of 
-- current letter.
relToAbsSystem ((c,v):rest) = (c,prevabsvalue*v):subsystem
  where subsystem = relToAbsSystem rest
        prevabsvalue = snd $ head $ subsystem
        
--The two systems used in this example
acsystem     = relToAbsSystem [('C',8),('L',2),('X',8),('V',2),('I',1)]
agentKsystem = relToAbsSystem [('C',2),('L',5),('X',2),('V',5),('I',1)] 

--Convert a Number into a System
toSystem :: System -> Int -> String
toSystem _ 0 = []
--This would happen if the last letter is bigger than the rest of the number.
toSystem [] number = error $ "rest of " ++ show number ++ " could not be translated to system" 
--For each letter of the system check, how often it is needed. Take that many repetitions of that char and prepend to the result of the recursion.
toSystem ((c,v):rest) number = 
  (replicate repetitions c) ++ toSystem rest (number-repetitions*v)
     where repetitions = number `div` v

--Convert from the given Sytem to a Number
fromSystem :: System -> String -> Int
-- Add up the absolute values of all letters.
fromSystem system = foldl convert 0 
        where convert aggr c = case lookup c system of
                 Nothing -> error $ "Invalid char: "++[c]
                 Just value -> value + aggr

simpletests = TestList [
   "CLLLVVVVVVI" ~=? toSystem acsystem 365,
   "CCCLXV" ~=? toSystem agentKsystem 365
                       ]
--Check if toSystem and fromSystem are inverse funcions by running some numbers through them
range = [1..10^4]
testconsistency = range ~=? map  ((fromSystem agentKsystem) . (toSystem agentKsystem) ) 
                                      range


