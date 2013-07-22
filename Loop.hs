
module Loop (loop)

where

import Test.HUnit

loop :: a -> (a-> Bool) -> (a->a) -> a
loop input check body | not $ check input = input
                      | otherwise = loop (body input) check body



fak x = loop (1,1) 
             (\(r,n) -> n /= (x+1)) 
             (\(r,n) -> (r*n,n+1) )

testit = TestList [
   TestCase $ 10 @=? (loop 1 (/= 10) (+1)),
   TestCase $ (24,5) @=? (fak 4)

                  ]
