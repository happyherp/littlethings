import Data.List(sort)
import Utils(toDigits, fromDigits)

productconcat x 1 = toDigits x
productconcat x n = productconcat x (n-1) ++ toDigits (x*n)

isPandigital x n= ([1..9]==)  $ sort $ productconcat x n

pandigitals = [fromDigits (productconcat x n) | x <- [1..9876], 
                                                n <- [1..9], 
                                                isPandigital x n]
                                                
result = maximum pandigitals