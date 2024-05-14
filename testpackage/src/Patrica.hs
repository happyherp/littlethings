module Patrica where

import Data.Maybe (isNothing, maybeToList)
import Data.List (null, find)
import Control.Monad (liftM)


--k is one Element of the key, v the value.
type PTree k v = Node k v

data Node k v = Node (Maybe v) [Path k v]
 deriving Show
data Path k v = Path k (Node k v)
 deriving Show


mkEmpty :: PTree k v
mkEmpty = Node Nothing []

insert :: Eq k => PTree k v -> [k] -> v -> PTree k v
insert (Node mnodevalue paths) suffix value  
   -- We have arrived at our destination. Insert Value!
   | null suffix = Node (Just value) paths
   -- None of the subpaths has the same startkey as we do. Create new Path
   | isNothing (findPathMatch suffix paths)
       = Node mnodevalue (Path (head suffix) (insert mkEmpty (tail suffix) value):paths)  
   -- We have a Path that has the same startkey. Insert into that node. 
   | otherwise = Node mnodevalue (map f paths)
       where f (Path k node) | k == head suffix = Path k (insert node (tail suffix) value)
                             | otherwise = Path k node

findPathMatch :: Eq k => [k] -> [Path k v] -> Maybe (Path k v)
findPathMatch suffix = Data.List.find (\(Path k _) -> k == head suffix)

   
find :: (Eq k) => PTree k v -> [k] -> Maybe v 
find (Node mnodevalue paths) suffix  
  | null suffix = mnodevalue
  | otherwise =  findPathMatch suffix paths >>= \(Path _ node) -> Patrica.find node (tail suffix)  



union :: (Eq k) => PTree k v -> PTree k v -> PTree k v
union (Node val1 paths1) (Node val2 paths2) = Node (firstJust val1 val2) (pathunion paths1 paths2 )
   
   
   
pathunion :: (Eq k) => [Path k v]  -> [Path k v] -> [Path k v]
pathunion [] paths2 = paths2
pathunion (Path k node:rest) paths2 = 
 let newkey = isNothing (Data.List.find (\(Path k2 _) -> k2 == k) paths2)
     funion (Path k2 node2) | k2 == k = Path k (union node node2)
                            | otherwise =  Path k2 node2
     thisunion = if newkey then Path k node:paths2 
                           else map funion paths2
 in   pathunion rest thisunion
   
   
firstJust (Just a) _ = Just a
firstJust _ (Just a) = Just a
firstJust _ _ = Nothing    
   
   
   
toList :: PTree k v -> [v]
toList (Node v paths) = foldl (\xs (Path _ n) -> xs ++ toList n) 
                              (maybeToList v) paths   
   
oneNode = insert mkEmpty "Hello" "World"
twoNodes = insert oneNode "Helgoland" "Ist eine Insel"
threeNodes = insert twoNodes "A" "Nochwas"

intToList :: Int -> [Int]
intToList i = if rest == 0 then [digit] else intToList rest ++ [digit] 
   where digit = i `mod` 10
         rest = i `div` 10 
         
         
rangeinsert = foldl (\m i -> insert m (intToList i) i) mkEmpty         
         
millionKeys = rangeinsert [0..1000000]

main = print (show (Patrica.find millionKeys [1,2,3,4,5,6]))

