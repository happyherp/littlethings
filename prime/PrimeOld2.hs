--module Prime where 
import Data.List
import Data.List.Ordered
import Data.Ord(comparing)
  
  
import Debug.Trace(trace)  
  

--Composites, x, Primes
type State = ([Int], Int, [Int])

calcComposites x = map (x*) [2..x]

findPrimes x composites = [2..(x^2)] `minus` composites

step :: State -> State
step (comps, ox, primes) = let 
    x = ox +1
    newcomps = merge comps (calcComposites x)
    in (newcomps, x, findPrimes x newcomps)
   
      
--Data.List.Ordered.mergeAll hangs on circularly defined lists. 
myMergeAll :: Ord a => [[a]] -> [a]
myMergeAll = myMergeAllBy compare 


myMergeAllBy :: (a->a->Ordering) -> [[a]] -> [a]
myMergeAllBy _ [] = []
myMergeAllBy cmp ((x:xs):xxs) = x:(mergeBy cmp xs (myMergeAllBy cmp xxs))


