import Data.List(delete, (\\), union, group, elemIndices, nub)
import Data.Foldable(foldMap)
import Utils(primeFactors)

range = [2..20]
redundant = concat $ map (\x->delete x $ primeFactors x) range
relevant = range \\ redundant

count :: Eq a => a -> [a] -> Int
count x xs = length $ elemIndices x xs

unionCounting :: [Int] -> [Int] -> [Int]
unionCounting a b =  foldMap 
	(\factor -> replicate (max (count factor a) (count factor b)) factor) 
	(union (nub a) b)

allPrimeFactors = foldl unionCounting [] $map primeFactors range
smallestMultiple = foldl (*) 1 allPrimeFactors