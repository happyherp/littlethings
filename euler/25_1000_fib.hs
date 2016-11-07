import Data.List(findIndex)


fibSeq :: [Integer]
fibSeq = 1:1:(fibCont 1 1)
  where fibCont n0 n1 = let n2 = n0+n1
                        in n2:(fibCont n1 n2)
 
--need to add 1
result = findIndex ((>= 1000) . length . show)  fibSeq