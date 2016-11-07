import qualified Data.Map.Strict as Map
import Data.List(maximumBy)
import Data.Ord(comparing)


nextColl :: Int -> Int
nextColl n | even n = n `div` 2
           | otherwise = 3*n + 1
       
       
collSeq :: Int -> [Int]
collSeq n = (takeWhile (>1) (iterate nextColl n))  ++ [1] 

type StartToLength = Map.Map Int Int

updateMap ::StartToLength -> Int -> StartToLength
updateMap map n | Map.member n map = map
                | n == 1 = Map.insert 1 0 map
                | otherwise = case (Map.lookup next map) of 
                      Nothing -> updateMap (updateMap map next) n
                      Just nextLength -> Map.insert n (nextLength+1) map
                    where next = nextColl n
                    
buildMap [] = Map.empty
buildMap (x:xs) = updateMap (buildMap xs) x                   
                   
result = maximumBy (comparing snd) (Map.assocs $ buildMap [1..10^6])