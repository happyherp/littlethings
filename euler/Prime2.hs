module Prime2 where 

    
merge :: Ord a => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys) | x<=y      = x:(merge xs (y:ys))
                    | otherwise = y:(merge (x:xs) ys)
        
unmerge :: Ord a => [a] -> [a] -> [a]
unmerge all [] = all
unmerge [] _ = []
unmerge (x:xs) (y:ys) | x == y = unmerge xs ys
                      | x  < y = x:(unmerge xs (y:ys))
                      | x  > y = unmerge (x:xs) ys      
                      
-- returns a list of all products of the given factors in ascending order(assuming factors is ascending as well)    
allProducts factors = let 
    f n = let 
        (first:others) = products n factors
        more = f (n+1)
        in first:(merge others more)
    in f 2
      
--uses only at most n factors      
products 1 factors = factors
products n [] = []
products n factors = let 
   first = head factors
   (lowest:rest) = map (*first) (products (n-1) factors)
   withOneFactorLess = products n (tail factors)
   in lowest:(merge rest withOneFactorLess)
                
primes2 :: [Integer]
primes2 = unmerge [2..] (allProducts (2:primes2))