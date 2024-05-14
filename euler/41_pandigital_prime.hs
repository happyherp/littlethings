import Utils(permutate,fromDigits, pandigitals)
import Data.List(sort, find)
import Prime(isPrime)
   
pandigitals = 
   reverse $ map fromDigits $ concat $ 
   map (\n -> permutate [1..n]) [1..9]

result = find isPrime pandigitals