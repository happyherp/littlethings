-- riddle from https://www.youtube.com/watch?v=1lHDCAIsyb8

import Data.List(uncons, sort)
import Data.Maybe(catMaybes)

type Digits = [Integer]

rotate :: Integer -> Integer
rotate x = let digits = show x in read $ last digits :(init digits)

continue :: Digits -> Digits
continue x = 
    let newDigit = case x of 
                     (h:[]) -> h * 2 `mod` 10 
                     (h1:h2:x) -> h1 * 2 `mod` 10 + h2 * 2 `div` 10
    in newDigit:x
    
    
digitsToInt :: Digits -> Integer
digitsToInt (x:[]) = x
digitsToInt (x:xs) = x*10^(length xs) + digitsToInt xs    
    
hit :: Integer -> Bool    
hit n = rotate n == n*2

candidates :: [[Integer]]
candidates = map (\d -> map digitsToInt $ filter (\digits -> head digits /= 0) 
                                                 (iterate continue [d]))  [1..9]


transposeInf :: [[a]] ->[[a]]
transposeInf  [] = []
transposeInf xss = let headTails = catMaybes $ map uncons xss
                       heads = map fst headTails
                       tails = map snd headTails
                   in heads : (transposeInf tails) 


candidatesOrdered :: [Integer]
candidatesOrdered =  concat $ map sort $ transposeInf candidates

rotateDoubles = filter hit candidatesOrdered

firstHit = head rotateDoubles
