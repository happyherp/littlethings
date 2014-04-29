module Automaten where

import Data.List
import qualified Data.Set as Set
import Data.Maybe (fromJust)

import Debug.Trace (trace)


type Q  a = [a]  --Alle zustände
type Z b = [b]  --Eingabealphabet
type S a b = [(a,Wort b,a)] --Übergangsfunktion
type I a = [a]--Startzustände
type F a = [a]   --Endzustaände
type Wort b = [b]

type EA a b = (Q a,Z b, S a b, I a, F a) --Automat


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

--Transform a State Machine so that it has a single start state.
unifyStartState :: Enum a => Ord a => Ord b => EA a b -> EA a b
unifyStartState ea | not (isValidAutomat ea) = error "Invalid state machine"
unifyStartState (q,e,s,i,f) | length i == 1 = (q,e,s,i,f)
unifyStartState (q,e,s,i,f)  = ((newstart:q),e,newtrans, [newstart], f)
   where newstart = succ $ maximum q
         newtrans = s ++ (map (\i -> (newstart,[],i)) i)

--Removes Transitions that return to the same state with the empty word.
removeRedundantTransitions :: Ord a => Ord b => EA a b -> EA a b
removeRedundantTransitions ea | not (isValidAutomat ea) = error "Invalid state machine"
removeRedundantTransitions (q,e,s,i,f) = (q,e,sNew,i,f)
  where sNew = filter (\(q,a,p)-> q /= p && a /= []) s  

--Transforms the state machine such that no transition has a word with a length 
-- larger than one.
removeLongWords :: Enum a => Ord a => Ord b => EA a b -> EA a b
removeLongWords ea | not (isValidAutomat ea) = error "Invalid state machine" 
removeLongWords ea | not $ hasLongWords ea = ea
removeLongWords (q,e,s,i,f) = 
  let (p,(a:word),r) = fromJust $ find (\(_,a,_) -> length a > 1) s
      newState = succ $ maximum q
      sNew = [(p,[a],newState),(newState,word,r)] ++ (delete (p,(a:word),r) s)
      qNew = (newState:q)
  in removeLongWords (qNew,e,sNew,i,f)

-- Transform such that there are no Transitions with an empty word.
toSpelling :: Ord a => Ord b => EA a b -> EA a b  
toSpelling ea | not (isValidAutomat ea) = error "Invalid state machine"
toSpelling ea | isSpelling ea = ea
toSpelling (q,e,s,i,f) = 
  let (p,_,r) = fromJust $ find (\(_,a,_) -> a == []) s
      newTrans = map (\(_,a,t)->(p,a,t)) $ filter (\(t,_,u) -> t == r ) s
      sNew = nub $ newTrans ++ (delete (p, [], r) s)
      fNew = if r `elem` f then (p:f) else f
  in toSpelling (q,e,sNew,i,fNew)
   

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
   
   
--helpers
isSubsetOf a b = (Set.fromList a) `Set.isSubsetOf` (Set.fromList b)
     