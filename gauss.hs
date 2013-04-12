-- Programm that uses  the gauss algorithm to solve linear equations.

import Test.HUnit

gauss :: Fractional a => [[a]] -> [a]

gauss eqs | True = undefined






tests = TestList (map (\(a, b, c) -> TestCase(assertEqual a b c)) [
       ("simple", gauss [[1.0,1.0]] ,[1.0]),       
       ("half",   gauss [[2.0,1.0]] ,[0.5]),
       ("hard",   gauss [[0.0, 3.0, 3.0, 2.0, 219.0],
                         [2.0, 2.0, 4.0, 4.0, 326.0],
                         [8.0, 4.0, 4.0, 0.0, 372.0],
                         [1.0, 2.0, 4.0, 3.0, 276.0]], [20.0,23.0, 30.0, 30.0])

   ])


runall = runTestTT tests






