--second attempt


import qualified Data.Set.Lazy as LSet

p :: Integer -> Integer
p n = n*(3*n-1) `div` 2


pentagonals :: [Integer]
pentagonals = map p [1..]


set = LSet.fromList pentagonals

isPent :: Integer -> Bool
isPent n = LSet.member n set

isPentPair (a,b) =   isPent(a+b) && isPent (abs(a-b)) 


pairs = [(p b,p a) | a<-[1..] , b<-[1..(a-1)]]