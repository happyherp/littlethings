sumSquare x = sum $ map (^2)[1..x]
squareSum x = (sum [1..x])^2
diff x = squareSum x - (sumSquare x)