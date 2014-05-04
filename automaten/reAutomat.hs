import Re
import Automaten
import Data.List
import Data.Maybe (fromJust)
import Test.HUnit

toNEA :: Eq a => RegExp a -> EA Integer a
toNEA (Terminal x) = ([1,2], [x], [(1,[x],2)], [1], [2]) 
toNEA (Sequence r1 r2) = 
  let (q1, e1, s1, i1, f1) = toNEA r1
      (q2, e2, s2, i2, f2) = makeDisjunct (toNEA r1) (toNEA r2)
      sNew =((head f1, [], head i2) : union s1 s2)
  in (union q1 q2, union e1 e2, sNew, i1, f2)  
toNEA (Alternative r1 r2) =     
  let (q1, e1, s1, i1, f1) = toNEA r1
      (q2, e2, s2, i2, f2) = makeDisjunct (toNEA r1) (toNEA r2)
      iNew = maximum (union q1 q2) + 1
      fNew = iNew + 1
      qNew = union q1 q2 ++ [iNew, fNew]
      sNew = union s1 s2 ++ [(iNew, [], head i1), (iNew, [], head i2), 
                             (head f1, [], fNew), (head f2, [], fNew)] 
   in (qNew, union e1 e2, sNew, [iNew], [fNew])
toNEA (Repetition r) = 
  let (q, e, s, i, f) = toNEA r
      fNew = maximum q + 1
      sNew = s ++ [(head i, [], fNew), (head f, [], fNew), (fNew, [], head i)]
  in (q ++ [fNew], e, sNew, i, [fNew])


  
matchRE :: (Ord a, Show a) => RegExp a -> [a] -> Bool  
matchRE re word = let dea = toDEAFull $ toNEA re
                  in matchDEA dea word
  

-- Return the second EA with all states renamed such that none has the same value
-- as a state in the first EA.
makeDisjunct :: EA Integer a -> EA Integer a -> EA Integer a
makeDisjunct (q1,_,_,_,_) (q2, e2, s2, i2, f2) = 
   let mapping = zip q2 (filter (\i -> notElem i q1) [1..])
       newState q = fromJust $ lookup q mapping
   in (map newState q2, e2, 
       map (\(p,a,q) -> (newState p, a, newState q)) s2,
       map newState i2, map newState f2)
      
ra = Terminal 'a'  
rb = Terminal 'b' 
rAorB = Alternative ra rb 
anyAorB = Repetition rAorB
aThenB = Sequence ra rb
anySeq = Sequence anyAorB anyAorB
anyAlt = Alternative anyAorB anyAorB
      
tests = TestList [
    False ~=? matchRE ra "",
    True  ~=? matchRE ra "a",
    False ~=? matchRE ra "b",
    False ~=? matchRE ra "aa",
    
    False ~=? matchRE rAorB "",
    True  ~=? matchRE rAorB "a",
    True  ~=? matchRE rAorB "b",
    False ~=? matchRE rAorB "c",
    False ~=? matchRE rAorB "ab",
    
    False ~=? matchRE aThenB "a",
    False ~=? matchRE aThenB "ba",
    False ~=? matchRE aThenB "abb",
    False ~=? matchRE aThenB "aab",
    False ~=? matchRE aThenB "aba",
    True  ~=? matchRE aThenB "ab",       
    
    True  ~=? matchRE anyAorB "",
    True  ~=? matchRE anyAorB "a",
    True  ~=? matchRE anyAorB "ab",
    True  ~=? matchRE anyAorB "bab",
    True  ~=? matchRE anyAorB "baaab",
    False ~=? matchRE anyAorB "baacab",
    False ~=? matchRE anyAorB "cbaaab",
    False ~=? matchRE anyAorB "baaabc",
    
    True  ~=? matchRE anySeq "",
    True  ~=? matchRE anySeq "a",
    True  ~=? matchRE anySeq "ab",
    True  ~=? matchRE anySeq "bab",
    True  ~=? matchRE anySeq "baaab",
    False ~=? matchRE anySeq "baacab",
    False ~=? matchRE anySeq "cbaaab",
    False ~=? matchRE anySeq "baaabc",

    True  ~=? matchRE anyAlt "",
    True  ~=? matchRE anyAlt "a",
    True  ~=? matchRE anyAlt "ab",
    True  ~=? matchRE anyAlt "bab",
    True  ~=? matchRE anyAlt "baaab",
    False ~=? matchRE anyAlt "baacab",
    False ~=? matchRE anyAlt "cbaaab",
    False ~=? matchRE anyAlt "baaabc"      
    
                  ]      