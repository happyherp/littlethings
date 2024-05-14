import Utils(permutate)

   
result = concat $ map show $ permutate [0..9] !! (10^6-1)