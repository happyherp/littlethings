module Prime2 where 
import Data.List.Ordered(member)
import Utils(merge, mergeBy)
import Data.Ord(comparing)
import qualified LazySet as LSet

   
unmerge :: Ord a => [a] -> [a] -> [a]
unmerge all [] = all
unmerge [] _ = []
unmerge (x:xs) (y:ys) | x == y = unmerge xs ys
                      | x  < y = x:(unmerge xs (y:ys))
                      | x  > y = unmerge (x:xs) ys      
                      
-- returns a list of all products of the given factors in ascending order(assuming factors is ascending as well)    
allProducts factors = let 
    f n = let 
        (first:others) = products n factors
        more = f (n+1)
        in first:(merge others more)
    in f 2
            
products n factors = map product (compositeTerms n factors)
                
                
--uses only at most n factors                     
compositeTerms :: Integral a => Int -> [a] -> [[a]]              
compositeTerms 1 factors = map (:[]) factors
compositeTerms n [] = []
compositeTerms n factors = let 
   first = head factors
   (lowest:rest) = map (first:) (compositeTerms (n-1) factors)
   withOneFactorLess = compositeTerms n (tail factors)
   in lowest:(mergeBy (comparing product) rest withOneFactorLess)
   
   
allCompositeTerms factors  = let 
    f n = let 
        (first:others) = compositeTerms n factors
        more = f (n+1)
        in first:(mergeBy (comparing product) others more)
    in f 2
    
primeTerms = allCompositeTerms primes
    
composites = allProducts (2:primes)
                
primes :: [Integer]
primes = unmerge [2..] composites

primeSet = LSet.fromList primes

isPrime :: Integer -> Bool
isPrime n = LSet.member n primeSet