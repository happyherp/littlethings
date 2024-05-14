import Utils(mergeBy)
import Data.Ord(comparing)
import Data.List(find, group)
import Debug.Trace(trace)

p :: Integer -> Integer
p n = n*(3*n-1) `div` 2


pentagonals :: [Integer]
pentagonals = map p [1..]

isPent :: Integer -> Bool
isPent i = (==i) $ head $ dropWhile (<i) pentagonals

isPentFast :: Integer -> Bool
isPentFast i = let 
    findUpperBound guess | p guess < i = findUpperBound (guess * 2)
                         | otherwise = guess
    upperBound = findUpperBound 10
    binSearch low guess high | p guess == i = True
                             | high - low <= 10 = any ((==i) . p) [low..high]
                             | p guess < i = binSearch guess ((guess+high)`div` 2) high
                             | otherwise = binSearch low ((guess+low)`div` 2) guess 
    in binSearch 1 (upperBound `div `2) upperBound

type Pair = (Integer, Integer)
    
isPentPair :: Pair  -> Bool
isPentPair (a,b) =   isPentFast(a+b) && isPentFast (abs(a-b)) 
    
n_dist_pairs :: Integer -> [Pair]
n_dist_pairs d = zip pentagonals (drop (fromInteger d) pentagonals) 

allPairs :: [Pair]
allPairs = let
    allpairs = map n_dist_pairs [1..]
    dist (a, b) = abs $ a - b
    combine (smallest:rest) bigger = smallest:(mergeBy (comparing dist) rest bigger)
    myfold (x:xs) =  combine x (myfold xs)
    in myfold allpairs

pairs2 = let 
    combineWithAllLower i = map (\p->(p, pentagonals !! i)) $take i pentagonals 
    in foldMap combineWithAllLower [2..]    

result = head $ filter isPentPair allPairs

perftest = map length $ group $ take 10000 $ map isPentPair allPairs

main = print result

