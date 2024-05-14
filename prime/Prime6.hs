import Data.List.Ordered(minus, merge)

mergeAll :: Ord a => [[a]] -> [a]
mergeAll [] = []
mergeAll ((x:xs):xxs) = x:(merge xs (mergeAll xxs))


multiplesOf n = iterate (+n) (2*n)
--Infinite List of the Lists of multiples of all prime numbers.
primemultiples= map multiplesOf primes
composites    = mergeAll primemultiples
primes        = [2,3]++([4..] `minus` composites)

main = print $ sum $ takeWhile (< 10^5) primes