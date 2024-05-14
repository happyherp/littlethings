
import  Game
import Data.List

bruteForce :: Pos -> [Checkpoint] -> [Pos]
bruteForce start [] = [start]
bruteForce start chpts = minimumBy shortest (map trypath chpts)
   where trypath chpt = let pos = start `addPos` mindist start chpt
                        in [start]++(bruteForce pos (filter (/= chpt) chpts))
         shortest a b = compare (distLength a) (distLength b)



distLength :: [Pos] -> Int
distLength [] = 0
distLength [x] = 0
distLength (x:y:ps) = (both (absPos (distPos x y))) + length (y:ps)
  where both (x,y) = x+y
