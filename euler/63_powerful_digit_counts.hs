import Data.List

digitPowers :: Integer -> Int
digitPowers n = let
    powers = map (^n) [1..]
    toSmall  i = i < 10^(n-1)
    notToBig i = i < 10^n
    in length $ takeWhile notToBig $ dropWhile toSmall powers  

    
result = sum $ takeWhile (> 0) $ map digitPowers [1..]