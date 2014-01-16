module Patrica where

import Data.Maybe (isNothing)
import Data.List (null, find)
import Control.Monad (liftM)


--k is the key(part), v the value.
type PTree k v = Node k v

data Node k v = Node (Maybe v) [Path k v]
 deriving Show
data Path k v = Path k (Node k v)
 deriving Show


mkEmpty :: PTree k v
mkEmpty = Node Nothing []

insert :: Eq k => PTree k v -> [k] -> v -> PTree k v
insert (Node mnodevalue paths) suffix value  
   | null suffix = Node (Just value) paths
   | isNothing (findPathMatch suffix paths)
       = Node mnodevalue (Path (head suffix) (insert mkEmpty (tail suffix) value):paths)   
   | otherwise = Node mnodevalue (map f paths)
       where f (Path k node) | k == head suffix = Path k (insert node (tail suffix) value)
                             | otherwise = Path k node

findPathMatch :: Eq k => [k] -> [Path k v] -> Maybe (Path k v)
findPathMatch suffix = Data.List.find (\(Path k _) -> k == head suffix)

   
find :: (Eq k) => PTree k v -> [k] -> Maybe v 
find (Node mnodevalue paths) suffix  
  | null suffix = mnodevalue
  | otherwise =  findPathMatch suffix paths >>= \(Path _ node) -> Patrica.find node (tail suffix)  

   
   
   
oneNode = insert mkEmpty "Hello" "World"
twoNodes = insert oneNode "Helgoland" "Ist eine Insel"
threeNodes = insert twoNodes "A" "Nochwas"