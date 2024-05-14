import Utils(primesUntil)
import Data.List.Ordered(member)
import Data.List(maximumBy)
import Data.Ord (comparing)

primes :: [Int]
primes = primesUntil (10^6)
isPrime p = p `member` primes

quad a b x = x^2+ a*x + b

primeCount :: Int -> Int -> Int
primeCount a b = length $ takeWhile (isPrime) (map (quad a b) [0..])

primeGenerators = [(a,b,primeCount a b) | a <- [-999..999], 
                                          b <- filter isPrime [-999..999]]
result = maximumBy (comparing (\(_,_,p) -> p))  primeGenerators                                           
                                           