import Prime8
import Utils(toDigits, fromDigits)
import Data.List (splitAt, takeWhile, nub)
import qualified Data.Set as S
import Data.Algorithm.MaximalCliques


findPairsForPrime :: Int -> [(Int, Int)]
findPairsForPrime p | not $ isPrime p = error "p must be prime."
findPairsForPrime p = let
    digits = toDigits p 
    candidates = map (\i-> splitAt i digits) [1..(length digits - 1)]
    toNum (p1,p2) = (fromDigits p1, fromDigits p2)
    in map toNum $ filter isPairPrime candidates
    
    
isPairPrime (p1, p2) = isPrime (fromDigits (p1++p2)) && 
                       isPrime (fromDigits (p2++p1)) &&  
                       isPrime (fromDigits p1) && 
                       isPrime (fromDigits p2) &&
                       head p1 /= 0 && head p2 /= 0
    
    
pairs = foldMap findPairsForPrime $ map fromIntegral primes    

--somepairs = takeWhile (/= (100003,7)) pairs
somepairs = take 64000 pairs

set = S.fromList $ map smallerFirst somepairs

smallerFirst (a,b) | a>b = (b,a)
smallerFirst x = x

nodes = nub $ foldMap (\(a,b)-> [a,b]) $ S.toList set

cliques = getMaximalCliques hasEdge nodes

hasEdge a b | a > b = hasEdge b a
            | otherwise = (a,b) `S.member` set
            
result = map sum $ filter ((== 5) . length) cliques