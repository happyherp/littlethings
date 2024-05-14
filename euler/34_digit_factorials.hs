import Data.Char(digitToInt)
import Data.List(find)
import Data.Maybe(fromJust)
import Data.Function.Memoize(memoFix)

fac :: Int -> Int
fac = let
  f1 f 0 = 1
  f1 f n = n * (f (n-1)) 
  in memoFix f1 

digitFactorial :: Int -> Int
digitFactorial n = sum (map (fac . digitToInt) (show n))

niners = iterate (\x -> x*10+9) 9

upperLimit = fromJust $ find (\x->digitFactorial x < x) niners



curious =filter (\x-> x == digitFactorial x) [10..upperLimit]

result = sum curious