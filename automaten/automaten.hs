import qualified Data.Set as Set

type Q  a = [a]  --Alle zustände
type Z a = [a]  --Eingabealphabet
type S a b = [(a,b,a)] --Übergangsfunktion
type I a = [a]--Startzustände
type F a = [a]   --Endzustaände
type Wort a = [a]

type EA a b = (Q a,Z b, S a b, I a, F a) --Automat

isValidAutomat :: Ord a => Ord b => EA a b -> Bool
isValidAutomat (q,z,s,i,f) = i `isSubsetOf` q 
                          && f `isSubsetOf` q 
                          && all (\(a,b,c) -> a `elem` q && b `elem` z && c `elem` q) s
                                 
accepts :: Ord a => Ord b => EA a b -> [b] -> Bool
accepts (q,z,s,i,f) input = any (accepts' s f input) i      

accepts' :: Ord a => Ord b => S a b -> F a -> Wort b -> a -> Bool
accepts' _ fstates [] curstate = curstate `elem` fstates
accepts' s fstates (x:xs) curstate = 
  let transitions = filter (\(a,b,_) -> a == curstate && b == x) s
      newstates = map (\(_,_,s)->s) transitions  
  in any (accepts' s fstates xs) newstates
                                 
                                 

a1 :: EA Char Int
a1 = ("abcde", [1..3], [('a', 1, 'b'),
                        ('a', 2, 'c'),
                        ('a', 3, 'e'),
                        ('b', 1, 'b'),
                        ('b', 3, 'e'),
                        ('e', 1, 'a'),
                        ('e', 1, 'b'),
                        ('e', 2, 'c'),
                        ('a', 2, 'c'),
                        ('b', 2, 'c')], "ab", "be")       


a2 :: EA Char Int
a2 = ( "a", [1],[('a', 1, 'a')], "a", "a")   


--helpers
isSubsetOf a b = (Set.fromList a) `Set.isSubsetOf` (Set.fromList b)
     