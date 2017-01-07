module Prime8 where 
import Data.List.Ordered(minus, mergeBy, merge)
import Data.Ord(comparing)
import qualified Data.Set.Lazy as LSet


products :: Integral a =>[a] -> Int -> [a]
products factors 1 = factors
products []      n = []
products factors n = let 
   (lowest:rest) = map (* head factors) (products factors (n-1))
   withHigherFactors = products (tail factors) n
   in lowest:(merge rest withHigherFactors)
   
   
composites = myMergeAll $  map (products primes) [2..]   
primes = [2,3]++([4..] `minus` composites)


myMergeAll = myMergeAllBy compare

myMergeAllBy :: (a->a->Ordering) -> [[a]] -> [a]
myMergeAllBy _ [] = []
myMergeAllBy cmp ((x:xs):xxs) = x:(mergeBy cmp xs (myMergeAllBy cmp xxs))


primeSet = LSet.fromList primes

isPrime :: Integral a => a -> Bool
isPrime n = LSet.member (fromIntegral n) primeSet


isPrimeSparse  :: Integral a => a -> Bool
isPrimeSparse n0 = all (not . divisible) candidates
    where n = fromIntegral n0 
          divisible p = n `mod` p == 0
          candidates = takeWhile (\p -> p*p <= n) primes