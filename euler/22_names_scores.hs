import Data.List(sort)
import Data.Char(ord)

main = do content <- readFile "p022_names.txt"
          let names = sort $ read ("["++content++"]")::[String]
          let alphaAndPosition = zip (map alphaVal names) [1..] :: [(Int, Int)]
          let scores = map (uncurry (*)) alphaAndPosition
          return $ sum scores 
          
alphaVal = sum . (map charVal )
  where charVal c =  ord c - ord 'A' + 1