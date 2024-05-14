import Utils(primeFactors)
import Data.List(intersect, (\\))
import Data.Char(intToDigit)
import Debug.Trace(trace)

type Fraction = (Int,Int)

cancelFraction :: Fraction -> Fraction
cancelFraction (n, d) = (n`div` cancelWith, d `div` cancelWith)
   where common = intersect (primeFactors n) (primeFactors d)
         cancelWith = foldl (*) 1 common

         
--Here I misunderstood the exercise. This compares the given fraction only to its most simplified version.               
isDigitCancelX :: Fraction -> Bool
isDigitCancelX fraction =let simplified = cancelFraction fraction
                             (n,d) = fraction
                             (sn,sd) = simplified
                             canceled = simplified /= fraction   
                             top    = [n `div` 10, n `mod` 10]
                             bottom = [d `div` 10, d `mod` 10]
                             common = intersect top bottom                                                    
                          in ( canceled
                               && sn < 10 && sd < 10  --is Single Digit
                               && (length   common) == 1
                               &&    top \\ common == [sn]
                               && bottom \\ common == [sd])
                               

toFractional :: Fractional a => Fraction -> a                               
toFractional (n,d) = fromIntegral n / fromIntegral d

            
isDigitCancel :: Fraction -> Bool                        
isDigitCancel (n,d) = let  
     top    = [n `div` 10, n `mod` 10]
     bottom = [d `div` 10, d `mod` 10]
     common = intersect top bottom             
     in length common == 1 && let 
        sn = head (top    \\ common)
        sd = head (bottom \\ common)
        in sd /= 0 && toFractional (n,d) == toFractional (sn,sd)
                        
curious_fractions = filter isDigitCancel [(n,d) | d <- [10..99], 
                                                  n <- [10..(d-1)],
                                                  d `mod` 10 > 0]
                                                    
                             
result = let 
    prodN = product (map fst curious_fractions)
    prodD = product (map snd curious_fractions)
    in snd $ cancelFraction (prodN, prodD)
 