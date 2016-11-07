import Data.List(nub)

powers maxa maxb = nub [a^b| a <- [2..maxa],
                             b <- [2..maxb]]
                           
result = length $  powers 100 100                           