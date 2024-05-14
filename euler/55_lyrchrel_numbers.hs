import Utils
import Data.List

revAdd n = n + (fromDigits $ reverse $ toDigits n) 

isPalin n = toDigits n == (reverse $ toDigits n )

isLychrel n = null $ filter isPalin $ take 50 $ tail $ iterate revAdd n 

result = length $ filter isLychrel [1..10000]