module Prime where 

import Data.List(takeWhile)

isPrime :: Integral a => a -> Bool
isPrime n = let
   possibleFactors = takeWhile (\factor -> factor^2 <= n) primes
   in not $ any (\factor -> n `mod` factor == 0) possibleFactors

primes :: Integral a => [a]
primes = 2:(primesStartingAt 3)

primesStartingAt :: Integral a => a -> [a]
primesStartingAt n = if isPrime n then n:followingPrimes 
                                  else followingPrimes
   where followingPrimes = primesStartingAt (n+1)