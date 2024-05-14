import Utils(primeFactors,mergeBy)
import Data.List(nub,group)
import Prime2(primeTerms,primes)
import Data.Ord(comparing)

exponentiate fs = map product $ group fs


neighbours :: [a] -> Int ->[[a]]
neighbours xs l | length (take l xs) < l = []
                | otherwise = (take l xs):(neighbours (tail xs) l)


differentFactors d n= let
    factors = map (exponentiate . primeFactors) [n..n+(d-1)]
    allHaveRightLength = all ((==d).length) factors
    allprimefactors = concat factors 
    in allHaveRightLength && (nub allprimefactors == allprimefactors)


result1 = head $ filter (differentFactors 4) [1..]



----Faster solution

allTerms = mergeBy (comparing product) (map (:[]) primes) primeTerms
findConsecutives n =  filter (isConsecutive n) $ neighbours allTerms n

isConsecutive :: Integral a =>  Int -> [[a]] -> Bool
isConsecutive n xss = let 
    groupWithExponents = map exponentiate xss
    haveRightSize = all ((==n) . length) groupWithExponents 
    together = concat groupWithExponents
    in haveRightSize && together == (nub together)

result2 = product $ head $ head $ findConsecutives 4

    
    
main = print result2