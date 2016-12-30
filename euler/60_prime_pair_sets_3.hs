import Prime8
import Utils(toDigits, fromDigits)
import Data.List (splitAt)


findPairsForPrime :: Int -> [(Int, Int)]
findPairsForPrime p = let
    digits = toDigits p 
    candidates = map (\i-> splitAt i digits) [1..(length digits - 1)]
    toNum (p1,p2) = (fromDigits p1, fromDigits p2)
    in map toNum $ filter isPairPrime candidates
    
    
isPairPrime (p1, p2) = let
    in isPrime(fromDigits (p2++p1)) &&  isPrime (fromDigits p1) && isPrime (fromDigits p2)
    
    
pairs = foldMap findPairsForPrime $ map fromIntegral primes    