
import Data.String(lines, words)
import Data.Foldable(fold)
import Data.Maybe (catMaybes)

type Grid = [[Int]]
type Pos = (Int, Int)
type Direction = (Pos -> Pos)

size = 20

gridIO = do content <- readFile "11.dat"
            let strGrid = map words $ lines content 
            let intGrid = map (map read) strGrid  ::Grid
            return intGrid
            
at :: Grid -> Pos -> Maybe Int
at grid (x, y) | 0 <= x 
                && x < size 
                && 0 <= y
                && y < size =  Just $ (grid !! y) !! x
               | otherwise = Nothing
            
            

right :: Direction 
right (x,y) = (x+1,y)   

down :: Direction
down (x,y) = (x,y+1)

downRight :: Direction
downRight (x,y) = (x+1,y+1)    

downLeft :: Direction
downLeft (x,y) = (x-1, y+1)     
            
            
adjAt :: Grid -> Pos -> Direction -> Int -> [Maybe Int]        
adjAt grid pos direction 0 = []
adjAt grid pos direction length = this:rest
   where this = at grid pos
         rest = adjAt grid (direction pos) direction (length -1)
         
 
allAdjAt :: Grid -> Pos -> [[Maybe Int]]
allAdjAt grid pos = map (\d -> adjAt grid pos d 4) directions
    where directions = [right, down, downRight, downLeft]
       
       

       
allAdj :: Grid -> [[Maybe Int]]
allAdj grid = fold [allAdjAt grid (x,y) |x<-[0..(size-1)], y <-[0..(size-1)]  ]   
     
     
     
biggestProduct :: [[Maybe Int]]  -> Int
biggestProduct adjacents = maximum $ map (product.catMaybes)  adjacents
  where product = foldl (*) 1
  
  
result = do grid  <- gridIO
            let adjacents = allAdj grid
            return $biggestProduct adjacents