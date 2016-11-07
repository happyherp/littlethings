type Triangle = [[Int]]      
type Pos = (Int, Int)   
   
smallTriange :: Triangle
smallTriange = [
    [3],
   [7,4],
  [2,4,6],
 [8,5,9,3]]
 
triangleIO :: IO [[Int]]
triangleIO = do content <-  readFile "18.dat"
                let tokenized = (map words (lines content))
                return $ map (map read) tokenized
                
at :: Triangle -> Pos -> Int
at triangle (x,y) = (triangle !! y) !! x

parents ::  Pos -> [Pos]
parents (x,0) = []
parents (0,y) = [(0,y-1)]
parents (x,y) | x == y = [(x-1,y-1)]
              | otherwise = [(x,y-1),(x-1,y-1)]
              
              
maxPathSum :: Triangle -> Pos -> Int
maxPathSum triangle pos = 
    let own = triangle `at` pos
        bestsubpath = foldl max 0 (map (maxPathSum triangle) (parents pos))
    in own + bestsubpath

 
resultIO :: IO Int
resultIO = do triangle <- triangleIO
              let lastRow = (length triangle) -1
              let endPositions = [(x,lastRow)|x<-[0..lastRow]]
              return $ maximum $ map (maxPathSum triangle) endPositions