import Prime8
import Utils
import Data.List
import Data.Ord


type Mask = [Bool]

buildMasks :: Int -> [Mask]
buildMasks 0 = [[]]
buildMasks size = foldMap (add) (buildMasks (size-1))
    where add smaller =  [True:smaller, False:smaller]

    
useMask :: [Int] -> Mask -> Int -> [Int]
useMask digits mask replacement=  map replace $ zip digits mask 
    where replace (_,True) = replacement
          replace (d,False) = d
          
          
useMask10 digits mask = map (useMask digits mask) [0..9]   


biggestFamily n =filter (isPrime . fromIntegral ) $  maximumBy (comparing primeCount) $ families n

sizeOfBiggestPrimeFamily n = maximum $ map primeCount $ families n

primeCount family = length $ filter (isPrime . fromIntegral) family


families n = let 
    digits = toDigits n       
    masks = filter or $ buildMasks (length digits)
    noLead0 = (/= 0) . head
    in map ((map fromDigits) . (filter noLead0)  . (useMask10 digits)) masks 
    
    
with8 = filter ((== 8) . length ) $ map (biggestFamily . fromIntegral) primes     
result = head $ head  with8 
