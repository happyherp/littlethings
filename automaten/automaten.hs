module Automaten where

import Data.List
import qualified Data.Set as Set
import Data.Maybe (fromJust)
import Data.Ord (comparing)

import Test.HUnit


import Debug.Trace (trace)


type Q  a = [a]  --Alle zustände
type Z b = [b]  --Eingabealphabet
type S a b = [(a,Wort b,a)] --Übergangsfunktion
type I a = [a]--Startzustände
type F a = [a]   --Endzustaände
type Wort b = [b]

type EA a b = (Q a,Z b, S a b, I a, F a) --Automat

--Transition access helper
source (q,_,_) = q
input  (_,a,_) = a
dest   (_,_,q) = q

isValidAutomat :: Ord a => Ord b => EA a b -> Bool
isValidAutomat (q,z,s,i,f) = 
  i `isSubsetOf` q && f `isSubsetOf` q 
  && all (\(p,a,r) -> p `elem` q && all (\e -> elem e z) a && r `elem` q) s
   
isDEA :: Ord a => Ord b => EA a b -> Bool
isDEA (q, e, s, i, f) = isValidAutomat (q, e, s, i, f) && length i == 1 
                        && isSpelling (q, e, s, i, f)
                        && not (hasMultipaths (q, e, s, i, f))
                        
isSpelling :: Ord a => Ord b => EA a b -> Bool
isSpelling (q, e, s, i, f) =  all (\(_, a, _) -> length a == 1) s   

isAlphabetical :: Ord a => Ord b => EA a b -> Bool
isAlphabetical (q, e, s, i, f) =  all (\(_, a, _) -> length a <= 1) s   

hasLongWords :: Ord a => Ord b => EA a b -> Bool
hasLongWords (q, e, s, i, f) =  any (\(_, a, _) -> length a > 1) s   

hasMultipaths :: Ord a => Ord b => EA a b -> Bool
hasMultipaths (q, e, s, i, f) =  any (\(p,a,q) -> any (\(p2,a2,q2)->(p,a) == (p2,a2) && q /= q2) s) s   

hasSingleStart :: EA a b -> Bool
hasSingleStart (_,_,_,i,_) = length i == 1

normalize :: (Ord a, Ord b) => EA a b -> EA a b
normalize (q, e, s, i, f) = (sort q, sort e, sort s, sort i, sort f)

--Transform a State Machine so that it has a single start state.
unifyStartState :: Enum a => Ord a => Ord b => EA a b -> EA a b
unifyStartState ea | not (isValidAutomat ea) = error "unifyStartState: Invalid state machine"
unifyStartState (q,e,s,i,f) | length i == 1 = (q,e,s,i,f)
unifyStartState (q,e,s,i,f)  = ((newstart:q),e,newtrans, [newstart], f)
   where newstart = succ $ maximum q
         newtrans = s ++ (map (\i -> (newstart,[],i)) i)

--Removes Transitions that return to the same state with the empty word.
removeRedundantTransitions :: Ord a => Ord b => EA a b -> EA a b
removeRedundantTransitions ea | not (isValidAutomat ea) = error "removeRedundantTransitions: Invalid state machine"
removeRedundantTransitions (q,e,s,i,f) = (q,e,sNew,i,f)
  where sNew = filter (\(q,a,p)-> q /= p || a /= []) s  

--Transforms the state machine such that no transition has a word with a length 
-- larger than one.
removeLongWords :: Enum a => Ord a => Ord b => EA a b -> EA a b
removeLongWords ea | not (isValidAutomat ea) = error "removeLongWords: Invalid state machine" 
removeLongWords ea | not $ hasLongWords ea = ea
removeLongWords (q,e,s,i,f) = 
  let (p,(a:word),r) = fromJust $ find (\(_,a,_) -> length a > 1) s
      newState = succ $ maximum q
      sNew = [(p,[a],newState),(newState,word,r)] ++ (delete (p,(a:word),r) s)
      qNew = (newState:q)
  in removeLongWords (qNew,e,sNew,i,f)

-- Transform such that there are no Transitions with an empty word.
toSpelling :: Ord a => Ord b => EA a b -> EA a b  
toSpelling ea | not (isValidAutomat ea) = error "toSpelling: Invalid state machine"
toSpelling ea | isSpelling ea = ea
toSpelling (q,e,s,i,f) = 
  let (p,_,r) = fromJust $ find (\(_,a,_) -> a == []) s
      newTrans = map (\(_,a,t)->(p,a,t)) $ filter (\(t,_,u) -> t == r ) s
      sNew = nub $ newTrans ++ (delete (p, [], r) s)
      fNew = if r `elem` f then (p:f) else f
  in toSpelling (q,e,sNew,i,fNew)
   
-- Takes a statemachine that is already spelling and has a single start state and 
-- transforms it, such that there is at most one way to go from each state. 
toDEA :: (Show a, Show b) =>  Enum a => Ord a => Ord b => EA a b -> EA a b   
toDEA ea | not (isValidAutomat ea) = error "toDEA: Invalid state machine"
toDEA ea | not (isSpelling ea)     = error "state machine must be spelling"
toDEA ea | not (hasSingleStart ea) = error "state machine must have a single start state"
toDEA (q,e,s,i,f) = renameStates $ completeDEA (q,e,s,i,f) ([i],e,fromStart,[i], newF)
  where fromStart = joinStates s i
        newF = if head i `elem` f then [i] else []

-- Builds a DEA from a NEA by following the missing transitions 
-- of the incomplete DEA that is passed in. Beeing incomplete means that not all 
-- states in the transition-table are in the state-set. Which indicates that transitions from 
-- those missing states are also not in the transition-table.
completeDEA :: (Show a, Show b) => Ord a => Ord b => EA a b -> EA [a] b -> EA [a] b
completeDEA (qo, eo, so, io, fo) 
            (qt, et, st, it, ft) | trace ("completeDEA:" ++ show (qt, et, st, it, ft)) False = undefined
                                 | not $ all (\(p,_,_)->p `elem` qt) st = error "Got a source state in a transition that was not in the state-set. "
                                 | not $ isIncompleteDEA (qt, et, st, it, ft) = error "Second FSM must already be a deterministic."
                                 | not $ it `isSubsetOf` qt = error "start states must be in state-set."
                                 | not $ ft `isSubsetOf` qt = error "final states must be in state-set."
                                 | all (\(_,_,p)->p `elem` qt) st = (qt, et, st, it, ft)
                                 | otherwise = 
  let missingStates = filter (\p -> not $ p `elem` qt) $ map dest st 
      newQ = qt ++ missingStates
      newS = st ++ (concatMap (joinStates so) missingStates) 
      newF = filter (\p -> p `intersect` fo /= []) newQ
  in completeDEA (qo, eo, so, io, fo) (newQ, et, newS, it, newF)  

-- Takes a list of Transitions and a Set of States. Returns a new List of transitions where the passed
-- State-Set is treated like a single State. It contains all transitions from that state's members to a set
-- of all states that a given input for any of states members leads to in the original transition-table.
joinStates :: Ord a => Ord b => [(a,b,a)] -> [a] -> [([a],b,[a])]
joinStates s sourcestate = 
    let outbound = filter ((`elem` sourcestate) . source) s
        groupedByInput = groupBy (\a b -> input a == input b) (sortBy (comparing input) outbound)
        newS = map (\t -> (sourcestate, input $ head t, nub $ map dest t)) groupedByInput
    in newS
    
-- Returns an identical state machine, except that the states that are sets are replaced with simple values.    
renameStates :: Enum a => Ord a => Ord b => EA [a] b -> EA a b
renameStates ea | not (isValidAutomat ea) = error "renameStates: Invalid state machine"
renameStates (q,e,s,i,f) = let mapping = zip q [(minimum (concat q))..]
                               convert s = case lookup s mapping of 
                                           Nothing -> error "renameStates did not find state in mapping"
                                           Just s -> s
                          in (map convert q, e, 
                              map(\(p,a,q) -> (convert p,a,convert q)) s, 
                              map convert i, map convert f) 

                              
toDEAFull :: (Show a, Show b) =>  Enum a => Ord a => Ord b => EA a b -> EA a b    
toDEAFull = toDEA . toSpelling . removeLongWords . removeRedundantTransitions . unifyStartState  


isIncompleteDEA ea = hasSingleStart ea && isSpelling ea && not (hasMultipaths ea)

-- Checks if the given DEA accepts a Word.
matchDEA :: (Ord a, Ord b) => EA a b -> [b] -> Bool
matchDEA ea word | not $ isDEA ea = error "matchDEA: FSM must be deterministic."
matchDEA (q,e,s,i,f) word = matchDEAInner (q,e,s,i,f) (head i) word

matchDEAInner :: (Ord a, Ord b) => EA a b -> a -> [b] -> Bool
matchDEAInner (q,e,s,i,f) state [] =  state `elem`  f
matchDEAInner (q,e,s,i,f) state (x:xs) = case find (\(p,a,q) -> p == state && x == head a) s of
                                           Nothing -> False
                                           Just (_,_,newstate) -> matchDEAInner (q,e,s,i,f) newstate xs
                              
sX = [('a',[2],'c'),('b',[1],'b'),('b',[3],'e'),('e',[2],'c'),
      ('b',[2],'c'),('a',[1],'f'),('f',[2],'c'),('f',[1],'b'),('f',[3],'e'),('f',[2],'c'),
      ('e',[1],'g'),('g',[2],'c'),('g',[1],'b'),('g',[3],'e'),('g',[2],'c'),('g',[1],'f'),
      ('f',[1],'h'),('h',[2],'c'),('h',[1],'b'),('h',[3],'e'),('h',[2],'c'),('h',[1],'f')
	  ]                            

a1 :: EA Char Int
a1 = ("abcde", [1..3], [('a', [1], 'b'),
                        ('a', [2], 'c'),
                        ('a', [], 'e'),
                        ('b', [1,2], 'b'),
                        ('b', [3], 'e'),
                        ('e', [1], 'a'),
                        ('e', [1], 'b'),
                        ('e', [2], 'c'),
                        ('a', [2], 'c'),
                        ('b', [2], 'c')], "ab", "be")       


a2 :: EA Char Int
a2 = ( "a", [1],[('a', [1], 'a')], "a", "a")   
a3 :: EA Char Int
a3 = ( "a", [],[('a', [], 'a')], "a", "a")   

a4 :: EA Char Int
a4 = ("abcde", [1..3], [('a', [1], 'b'),
                        ('a', [2], 'c'),
                        ('a', [], 'e'),
                        ('b', [1], 'b'),
                        ('b', [3], 'e'),
                        ('e', [1], 'a'),
                        ('e', [1], 'b'),
                        ('e', [2], 'c'),
                        ('a', [2], 'c'),
                        ('b', [2], 'c')], "a", "be")     
   
a5 :: EA Char Int
a5 = ( "abc", [1],[('a', [], 'b'), ('b', [1], 'c')], "a", "c")  
   
a6 :: EA Char Int
a6 = ( "abc", [1,2],[('a', [1], 'b'), ('a', [1], 'c'), ('b', [2], 'a')], "a", "c")  
   
a7 :: EA Char Int
a7 = ( "abc", [1,2],[('a', [1], 'b'), ('a', [1], 'b'), ('b', [1], 'c'), ('b', [1], 'a')], "a", "b")  

a8 :: EA Char Int
a8 = ("abcde", [1..3], [('a', [1], 'b'),
                        ('a', [], 'e'),
                        ('b', [1], 'b'),
                        ('e', [1], 'a')], "a", "be")      
   
a9 :: EA Char Int
a9 = ( "abc", [1,2,3],[('a', [], 'b'), ('b', [1,2,3], 'c')], "a", "c")  

a10 = toDEAFull ("abc",[1,2],[('a', [1,1], 'b'),('b', [2,2], 'b'),('b', [1,1,1], 'c')], "a", "c")

a11 = ([1,2,3,4,5,6,7],"ab",[(1,"a",2),(3,"b",4),(5,"",1),
                                       (5,"",3),(2,"",6),(4,"",6),
                                       (5,"",7),(6,"",7),(7,"",5)
                            ],[5],[7])
                                      
   
--helpers
isSubsetOf a b = (Set.fromList a) `Set.isSubsetOf` (Set.fromList b)

--- TESTS ---
testTransformation = TestList [
   ("abcd", [1,2,3], [('a', [1], 'c'),('c', [2], 'd'),('d', [3], 'b')], "a", "b")
      ~=? normalize (removeLongWords ("ab", [1,2,3], [('a', [1,2,3], 'b')], "a", "b")),
      
   (("ab", [], [], "a", "ab")  :: EA Char Int)
      ~=? normalize (toSpelling ("ab", [], [('a', [], 'b')], "a", "b")),   
   (("abcd", [1], [('a', [1], 'b')], "a", "ab")  :: EA Char Int)
      ~=? normalize (toSpelling ("abcd", [1], [('a', [1], 'b'), ('c',[],'d'), ('d',[],'c')], "a", "b")),   
            
   ("ab", [1],[('a',[1], 'b'), ('b', [1], 'b')], "a", "b")   
      ~=? normalize (toDEA ("ab", [1],[('a',[1], 'b'), ('a', [1], 'a')], "a", "b"))
                 ]
  
acceptSingle =  toDEAFull ("ab", [1,2], [('a', [1], 'b')], "a", "b")  
acceptSome =    toDEAFull ("ab", [1,2], [('a', [1], 'b'), ('b',[],'a')], "a", "b")  
acceptAny =     toDEAFull ("a", [1], [('a', [1], 'a')], "a", "a")  

  
testMatch = TestList [
    False ~=? matchDEA acceptSingle [],
    False ~=? matchDEA acceptSingle [2],
    True  ~=? matchDEA acceptSingle [1],
    False ~=? matchDEA acceptSingle [1,1,1,1,1],
    False ~=? matchDEA acceptSingle [1,1,1,2],
    False ~=? matchDEA acceptSingle [2,1,1,1],
    
    False ~=? matchDEA acceptSome [],
    False ~=? matchDEA acceptSome [2],
    True  ~=? matchDEA acceptSome [1],
    True  ~=? matchDEA acceptSome [1,1,1,1,1],
    False ~=? matchDEA acceptSome [1,1,1,2],
    False ~=? matchDEA acceptSome [2,1,1,1],

    True ~=? matchDEA acceptAny [],
    False ~=? matchDEA acceptAny [2],
    True  ~=? matchDEA acceptAny [1],
    True ~=?  matchDEA acceptAny [1,1,1,1,1],
    False ~=? matchDEA acceptAny [1,1,1,2],
    False ~=? matchDEA acceptAny [2,1,1,1],

    False ~=? matchDEA a10 [],
    False ~=? matchDEA a10 [1],
    False ~=? matchDEA a10 [1,1,1,1],
    True  ~=? matchDEA a10 [1,1,1,1,1],
    False ~=? matchDEA a10 [1,1,1,1,1,1],
    False ~=? matchDEA a10 [1,1,2,1,1,1],
    True  ~=? matchDEA a10 [1,1,2,2,1,1,1],
    False ~=? matchDEA a10 [1,1,2,2,2,1,1,1],    
    True  ~=? matchDEA a10 [1,1,2,2,2,2,1,1,1]
                     ]    