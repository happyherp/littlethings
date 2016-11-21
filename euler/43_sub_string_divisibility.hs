import Utils(fromDigits,permutate)

pandigitals = 
   reverse $ permutate [0..9]
   
isInteresting digits = and [
    isSubStringDivisible digits [1..3] 2,
    isSubStringDivisible digits [2..4] 3,
    isSubStringDivisible digits [3..5] 5,
    isSubStringDivisible digits [4..6] 7,
    isSubStringDivisible digits [5..7] 11,
    isSubStringDivisible digits [6..8] 13,
    isSubStringDivisible digits [7..9] 17
    ]   
   
isSubStringDivisible digits range divisor = let
   substring = map (digits!!) range
   in (fromDigits substring) `mod` divisor == 0
   
result = sum $ map fromDigits $ filter isInteresting pandigitals