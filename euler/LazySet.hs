
module LazySet where 

import qualified Data.List as List

data LazySet a = Node [a] (LazySet a) | End 
    deriving (Show)

member :: Ord a => a -> LazySet a ->  Bool
member e End = False
member e (Node (x:xs) nextLevel) | x > e = False
                                 | otherwise = member e nextLevel || List.elem e (x:xs)

fromList :: Ord a => [a] -> LazySet a
fromList = f 0
    where f _ [] = End
          f level xs = let 
            (elementsForThisLevel, elementsFurtherDown) = List.splitAt (2^level) xs
            in Node elementsForThisLevel (f (level + 1) elementsFurtherDown)

infSet = fromList $ filter even [1..]