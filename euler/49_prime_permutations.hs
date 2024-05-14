import Utils
import Data.List
import Prime2
import Data.Maybe(mapMaybe)


kombinate :: Ord a => Int -> [a] -> [[a]]
kombinate 1 xs = map (:[]) xs
kombinate k xs = let 
    addEachMissingOne combination = let
        missingOnes = filter (<= minimum(combination)) xs
        in map (:combination) missingOnes
    in foldMap addEachMissingOne $kombinate (k-1) xs
    
kombinateNoRepeat :: Ord a => Int -> [a] -> [[a]]
kombinateNoRepeat 1 xs = map (:[]) xs
kombinateNoRepeat k xs = let 
    addEachMissingOne combination = let
        missingOnes = filter (< minimum(combination)) xs
        in map (:combination) missingOnes
    in foldMap addEachMissingOne $kombinateNoRepeat (k-1) xs    
    
findArithmetic ints = let 
    candidates = kombinateNoRepeat 3 $ filter (>999) ints
    sameDist [a,b,c] = b-a == c-b    
    in filter sameDist candidates
    
primePermutation term = filter isPrime $ map (fromIntegral .  fromDigits) $ permutate term
    
result = filter (not . null ) $ map (findArithmetic . primePermutation) $ kombinate 4 [0..9]    
