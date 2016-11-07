import Utils(primeFactors)
import Data.List(intersect)

cancelFraction :: Int -> Int -> (Int, Int)
cancelFraction n d = (n`div` cancelWith, d `div` cancelWith)
   where common = intersect (primeFactors n) (primeFactors d)
         cancelWith = foldl (*) 1 common


isDigitCancel :: Int -> Int -> Bool
isDigitCancel n d = let (cnclN,cnclD) = cancelFraction n d
                        [0..9]
                        