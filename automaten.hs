
import Data.Set (Set, singleton, empty, union, unions, member, insert)
import qualified Data.Set as Set


data RegExp a = Terminal a 
               | Sequence (RegExp a) (RegExp a)
               | Alternative (RegExp a) (RegExp a)
               | Repitition (RegExp a) 
   deriving Show
   
matches :: Ord a => (RegExp a) -> [a] -> Bool
matches r s =  member [] (matchPart r s)

matchPart ::  Ord a => (RegExp a) -> [a] -> Set [a]
matchPart (Terminal t) (x:xs) | t == x = singleton xs
matchPart (Terminal _) _ = empty
matchPart (Sequence r1 r2) xs = unions $ map (matchPart r2) 
                                             (Set.toList (matchPart r1 xs))
matchPart (Alternative r1 r2) xs = union (matchPart r1 xs) (matchPart r2 xs)
matchPart (Repitition r) xs = insert xs (matchPart (Sequence r (Repitition r)) xs)

r1 = Sequence (Terminal '1') (Repitition (Terminal '0'))
