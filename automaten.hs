
 import Data.Set (Set)


data RegExp a = Terminal a 
               | Sequence (RegExp a) (RegExp a)
               | Alternative (RegExp a) (RegExp a)
               | Repitition (RegExp a) 
   deriving Show
   
matches :: Eq a => (RegExp a) -> [a] -> Bool
matches r s =  elem [] (matchPart r s)

matchPart ::  Eq a => (RegExp a) -> [a] -> [[a]]
matchPart (Terminal t) (x:xs) | t == x = [xs]
matchPart (Terminal _) _ = []
matchPart (Sequence r1 r2) xs = concatMap (matchPart r2) (matchPart r1 xs )
matchPart (Alternative r1 r2) xs = matchPart r1 xs ++ matchPart r2 xs
matchPart (Repitition r) xs = [xs] ++ matchPart (Sequence r (Repitition r)) xs

r1 = Sequence (Terminal "1") (Repitition (Terminal "0"))
