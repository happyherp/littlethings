module Utils where 

import Data.Maybe(fromJust)
import Data.List(find, nub, delete)
import qualified Data.Set as Set (Set, fromList, empty, (\\), findMax, findMin, delete)

primeFactors 1 = []
primeFactors x =  let 
  smallestFactor = fromJust $ find (\d-> 0 == x `mod` d) [2..]
  divisor = x `div` smallestFactor
  in if (divisor == 1) then [smallestFactor] else primeFactors(smallestFactor)++primeFactors(divisor)
  
primesUntil :: Int -> [Int]
primesUntil 1 =  []
primesUntil 2 = [2]
primesUntil x = (reverse $ dividePrimes [2] (Set.fromList [3..x])) 

dividePrimes :: [Int] -> Set.Set Int -> [Int]
dividePrimes prime maybePrime | Set.empty == maybePrime = prime
                              | otherwise = 
   let highestPrime = head(prime)
       primeMultiples = takeWhile (<= (Set.findMax maybePrime)) [m*highestPrime | m <- [2..] ]
       newMaybePrime =  maybePrime Set.\\ (Set.fromList primeMultiples)
       newPrime = Set.findMin(newMaybePrime)
       in dividePrimes (newPrime:prime) (Set.delete newPrime newMaybePrime)
                      

dividents :: Int -> [Int]
dividents 1 = [1]
dividents x =  nub $ map (foldl (*) 1) 
                         (variate (primeFactors x))


  
variate :: [a] -> [[a]]       
variate [] = [[]]
variate (x:xs) = subpermutations++(map (x:) subpermutations)
   where subpermutations = variate xs
                            
quersumme :: Integer -> Integer
quersumme x =  sum $  map (read . (:[])) (show x)                      


keep :: (a->b) -> a -> (a,b)
keep f a = (a, f a)


permutate :: [Int] -> [[Int]]
permutate (x:[]) = [[x]]
permutate xs = concat $ map buildSub xs
   where buildSub x = map (x:) (permutate (delete x xs))