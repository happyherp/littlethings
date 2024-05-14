import Utils(dividents)
import Data.List(delete)

properDividents x = delete x (dividents x)


d x = sum $ properDividents x

isAmicable a = let b = d a
               in d b == a && a /= b

result = sum (filter isAmicable [2..(10000 -1)])