import Data.List(transpose)


halfDiagonal :: Int -> [Int]
halfDiagonal start =  next 1 (iterate (+8) start)
    where next prev (x:xs) = let cur = prev + x
                             in cur:(next cur xs)
                             
diagonals :: [Int]                             
diagonals = let fourDiagonals = map halfDiagonal [2,4,6,8]
                rings =  map sum (transpose fourDiagonals)
            in scanl (+) 1 rings             


spiralDiagonal size = diagonals !! ncircles
  where ncircles = ((size-1) `div` 2 )            
            
            
result = spiralDiagonal 1001