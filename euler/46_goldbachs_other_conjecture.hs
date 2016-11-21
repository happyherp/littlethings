import Prime2(isPrime,composites)


double_squares = map (\x->(x^2)*2) [1..]

isPrimeSquare n = let
    sq_candidates = takeWhile (<n) double_squares
    primesNeeded = map (n-) sq_candidates
    in any isPrime primesNeeded
    
    
odd_composites = filter odd composites    

result =  filter (not . isPrimeSquare) odd_composites