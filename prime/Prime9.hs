import Data.List.Ordered(minus, mergeBy, merge)



composites = tail $ iterate products primes


primes = 2:3:([4..] `minus` (myMergeAll composites))


products :: [Integer] -> [Integer]
products source = myMergeAll $ map (\prime -> map (* prime) source) primes


myMergeAll = myMergeAllBy compare

myMergeAllBy :: (a->a->Ordering) -> [[a]] -> [a]
myMergeAllBy _ [] = []
myMergeAllBy cmp ((x:xs):xxs) = x:(mergeBy cmp xs (myMergeAllBy cmp xxs))
