import Data.Char(ord)


trianglenumbers = map (\n->floor $ 0.5*n*(n+1)) [1..]


--isTriangleWord ::String -> Bool
isTriangleWord word = let 
  wordval = sum $ map (\c->ord c - (ord 'A') + 1) word
  isTriangle = (wordval==) $ head $ dropWhile (< wordval) trianglenumbers
  in isTriangle
  
  
result = do 
    content <- readFile "p042_words.txt"
    let words = (read ("["++content++"]"))::[String]
    return $ length $ filter isTriangleWord words