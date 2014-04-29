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

hasLongTransitions :: Ord a => Ord b => EA a b -> Bool
hasLongTransitions (q, e, s, i, f) =  all (\(_, a, _) -> length a > 1) s   

hasMultipaths :: Ord a => Ord b => EA a b -> Bool
hasMultipaths (q, e, s, i, f) =  any (\(p,a,q) -> any (\(p2,a2,q2)->(p,a) == (p2,a2) && q /= q2) s) s             

--Transform a NEA to a DEA. Does not do remove transitions with words > 1 
-- or multiple start states                        
toDEA :: Show a => Show b => Enum a => Ord a => Ord b => EA a b -> EA a b
toDEA ea | trace ("toDEA " ++ show ea) False = undefined
toDEA ea | isDEA ea = ea
toDEA ea | not (isValidAutomat ea) = error "Invalid state machine"
toDEA ea | length i /= 1 = error "Got a state machine with more than one startstate."
  where (q,e,s,i,f) = ea 
toDEA ea | hasLongTransitions ea= error "Got a state machine with transitions of length greater one."
toDEA ea | any (\(q,a,p)-> q == p && a == []) s = toDEA (q,e,sNew,i,f)
  where (q,e,s,i,f) = ea 
        sNew = filter (\(q,a,p)-> q /= p && a /= []) s
toDEA ea | not (isSpelling ea) = 
  let (q,e,s,i,f) = ea
      (p,_,r) = fromJust $ find (\(_,a,_) -> a == []) s
      newTrans = map (\(_,a,t)->(p,a,t)) $ filter (\(t,_,u) -> t == r ) s
      sNew = nub $ newTrans ++ (delete (p, [], r) s)
      fNew = if r `elem` f then (p:f) else f
  in toDEA (q,e,sNew,i,fNew)
toDEA ea | hasMultipaths ea = 
  let (q,e,s,i,f) = ea
      trans = head $ filter (\xs -> length xs > 1) (groupBy (\(q,a,_) (r,b,_) -> (q,a) == (r,b)) (sort s))
      transStates = map (\(_,_,q)-> q) trans
      newstate = succ $ maximum q
      (p, a, _) = head trans
      newtrans = map (\(_,a,r) -> (newstate, a, r)) $filter (\(p,_,_) -> elem p transStates) s
      sNew = nub $ (s \\ trans) ++ [(p,a,newstate)] ++ newtrans
      fNew = if any (\p -> elem p f) transStates then (newstate:f) else f
  in toDEA ((newstate:q), e, sNew, i, fNew)
        

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
   
--helpers
isSubsetOf a b = (Set.fromList a) `Set.isSubsetOf` (Set.fromList b)
     