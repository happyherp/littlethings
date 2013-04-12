-- Programm that uses  the gauss algorithm to solve linear equations.

import Test.HUnit
import Debug.Trace

gauss :: (Show a, Eq a, Fractional a) => [[a]] -> [a]



gauss eqs | any (\eq -> length eqs + 1 /= length eq) eqs  
    = error "number of equations != number of variables"
          | all ( (0.0==) .head) eqs = error "all leading zeros"
gauss [] =  error "no equations"          
--Only one equation
gauss [[a,r]] = [r/a]
--Switch until non 0 is in topleft corner
gauss eqs | head(head eqs) == 0.0 = gauss (tail eqs++[head eqs])
-- eliminate 
gauss eqs | otherwise = trace tracestring result 
  where topEq = head eqs
        submatrix = makeSubmatrix eqs
        --multiply the multipliers with the results from the submatrix
        inserted = zipWith (*) (init (tail topEq)) submatrixresult
        --calculate the last remaining variable
        missingvar = ((last topEq) - (sum inserted))/(head topEq)
        result = missingvar:submatrixresult
        submatrixresult = (gauss submatrix)
        tracestring = "eqs" ++ show eqs ++ "\nresult " ++ show result ++ "\nsubmatrix" ++ show submatrix


makeSubmatrix :: (Fractional a) => [[a]] -> [[a]]
makeSubmatrix eqs= submatrix
  where topEq = head eqs
        submatrix = map eliminate (tail eqs)
        eliminate (v:vs) = zipWith (-) vs topMult
            where topMult = map (* (v/(head topEq))) (tail topEq)


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






