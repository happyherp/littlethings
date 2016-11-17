import Utils(primesUntil)
import Data.List(elem)
import Data.Char(digitToInt, intToDigit)
import qualified Data.Set as Set

primes = Set.fromList (primesUntil (10^6))

rotations :: Int -> [Int]
rotations n = let 
   digits = map digitToInt (show n)
   rotate (x:xs) = xs++[x]
   rotatedLists = take (length digits) (iterate rotate digits)
   in map (read . (map intToDigit)) rotatedLists

isCircular n = all isPrime (rotations n)
    where isPrime n = Set.member n primes
    
    
circularPrimes = Set.filter isCircular primes

    
result = length circularPrimes    