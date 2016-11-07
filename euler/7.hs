import Data.List( (\\), partition )
import Data.Foldable(foldMap)
import Utils(primesUntil)

nthPrime :: Int->Int
nthPrime n=  (head (filter 
                           (\primes -> (length primes) >= n ) 
                           $ map (\x -> reverse (primesUntil (4^x))) [1..])) !! n
  

