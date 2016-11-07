import qualified Data.Map.Strict as Map
import Data.Function.Memoize

pathsTo :: Int -> Int -> Int
pathsTo x 0 = 1
pathsTo 0 y = 1
pathsTo x y = (pathsTo (x-1) y) + (pathsTo x (y-1))



type Coord = (Int, Int)
pathsToMem :: Map.Map Coord Int -> Coord -> (Map.Map Coord Int, Int)
pathsToMem map (x, 0) = (map, 1)
pathsToMem map (0, y) = (map, 1)
pathsToMem map pos = case (Map.lookup pos map) of
                       Just x -> (map, x)
                       Nothing -> let (x,y) = pos
                                      result = resultL + resultR
                                      mapNew = Map.insert pos result mapR
                                      (mapL, resultL) = pathsToMem map  (x-1,y  )
                                      (mapR, resultR) = pathsToMem mapL (x  ,y-1)
                                  in  (mapNew, result)
                          
result = snd $ pathsToMem Map.empty (20,20)


pathsToMem2 :: Int -> Int -> Int
pathsToMem2 = memoFix2 pathsTo
                  where pathsTo f x 0 = 1
                        pathsTo f 0 y = 1
                        pathsTo f x y = (f (x-1) y) + (f x (y-1))
                        
                        
result2 = pathsToMem2 30 30                     