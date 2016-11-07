import Data.List(elem)
import Data.Foldable(foldMap)

target = 1000

squares = map (^2 )[1..]
squaresBelow target = takeWhile (<= target) squares

buildTerm :: Int -> Int -> Int -> [[Int]]
buildTerm 1 target top= if (elem target (squaresBelow top)) then [[target]] else []
buildTerm n target top= t2
   where candidates = (squaresBelow (min target top))
         t1 = foldMap (\c -> map (c:) (buildTerm (n-1) (target - c) c)) 
                  candidates
         t2 = filter (/= []) t1
         
                  
         
resultSquares =  buildTerm 3 target target        
result = 