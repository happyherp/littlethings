-- Programm that uses the gauss algorithm to solve linear equations. Written by Carlos Freund

import Test.HUnit

gauss :: (Eq a, Fractional a) => [[a]] -> [a]

gauss eqs | any (\eq -> length eqs + 1 /= length eq) eqs  
    = error "number of equations != number of variables"
          | all ( (0.0==) .head) eqs = error "all leading zeros"
gauss [] =  error "no equations"          
--Only one equation. Solve directly
gauss [[a,r]] = [r/a]
--Switch until non 0 is in topleft corner
gauss eqs | head(head eqs) == 0.0 = gauss (tail eqs++[head eqs])
--Eliminate leftmost factors, then recurse
gauss eqs | otherwise = missingvar:submatrixresult 
  where topEq = head eqs
        submatrixresult = (gauss (submatrix eqs))
        --Multiply the factors with the results from the submatrix to get real value.
        real = sum (zipWith (*) (init (tail topEq)) submatrixresult)
        --Calculate the last remaining variable
        missingvar = ((last topEq) - real)/(head topEq)

--Create a smaller matrix by subtracting factors in the first row to zero.
submatrix :: (Fractional a) => [[a]] -> [[a]]
submatrix ((topleftfactor:toprest):lowereqs) = map eliminate lowereqs
   where eliminate (v:vs) = zipWith (-) vs topMult --subtract the lower equation with the (factored) top equation
          where topMult = map (* (v/topleftfactor)) toprest --multiply top-equation so that the first factor matches the other equation


tests = TestList (map (\(a, b, c) -> TestCase(assertEqual a b c)) [
       ("simple", [1.0], gauss [[1.0,1.0]]),       
       ("half",   [0.5], gauss [[2.0,1.0]]),
       ("2vars",  [5.0, -2.0], gauss [[1.0, 2.0, 1.0],[1.0,1.0,3.0]]),
       ("hard",   [20.0, 23.0, 30.0, 30.0], gauss [[0.0, 3.0, 3.0, 2.0, 219.0],
                                                   [2.0, 2.0, 4.0, 4.0, 326.0],
                                                   [8.0, 4.0, 4.0, 0.0, 372.0],
                                                   [1.0, 2.0, 4.0, 3.0, 276.0]] )

   ])

runall = runTestTT tests
