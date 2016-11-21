
module LazySet2 where 

import qualified Data.List as List
import qualified Data.Set as NSet

data LazySet a = Node (NSet.Set a) (LazySet a) | End 
    deriving (Show)

    
member :: Ord a => a -> LazySet a ->  Bool
member e End = False
member e (Node set nextLevel) = case NSet.lookupLE e set of 
    Nothing -> False
    Just x ->  x == e || member e nextLevel
                                 
                                

fromList :: Ord a => [a] -> LazySet a
fromList = f 0
    where f _ [] = End
          f level xs = let 
            (elementsForThisLevel, elementsFurtherDown) = List.splitAt (2^level) xs
            in Node (NSet.fromList elementsForThisLevel) (f (level + 1) elementsFurtherDown)

