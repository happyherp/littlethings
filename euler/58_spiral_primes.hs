import Prime8(isPrimeSparse, primes)
import Data.List
import Utils

halfDiagonal :: Int -> [Int]
halfDiagonal start =  next 1 (iterate (+8) start)
    where next prev (x:xs) = let cur = prev + x
                             in cur:(next cur xs)
                             
     
spiral = map (\(p,t,s,d)->(p,t,s)) $ iterate doSprial (0,1,1,map halfDiagonal  [2,4,6,8])



doSprial (primes, total, sidelength, diagonals) = let 
    new_primes = primes + (length $ filter (isPrimeSparse . fromIntegral) $ map head diagonals)
    new_total = total + length diagonals
    new_sidelength = sidelength + 2
    new_diagonals = map tail diagonals
    in (new_primes, new_total, new_sidelength, new_diagonals)
    
    
below10 (p,t,s) = fromIntegral p / (fromIntegral t) < 0.1
    
result = head $ filter below10 $ tail $ spiral