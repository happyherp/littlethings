import Utils(dividents)
import Data.List(delete)
import qualified Data.Set as Set

properDividents x = delete x (dividents x)

isAbundant n = sum(properDividents n) > n

limit = 28123 

abundant_numbers_set = Set.fromList abundant_numbers
abundant_numbers =  filter isAbundant [1..limit]

isSum :: Int->Bool
isSum = not . null . findSums 
                     
                     
findSums :: Int -> [(Int,Int)]
findSums n = map withPartner $ filter hasPartner $ takeWhile (< n) abundant_numbers                    
        where hasPartner a = Set.member (n-a) abundant_numbers_set
              withPartner a = (a, n-a)
        
result =  sum $ filter (not . isSum) [1..limit]