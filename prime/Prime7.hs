import Data.List.Ordered(minus, merge)

mergeAll :: Ord a => [[a]] -> [a]
mergeAll [] = []
mergeAll ((x:xs):xxs) = x:(merge xs (mergeAll xxs))


highMultiplesOf n = iterate (+n) (n*n)
primemultiples= map highMultiplesOf primes
composites    = mergeAll primemultiples
primes        = [2,3]++([4..] `minus` composites)


main = print $ sum $ takeWhile (< 10^5) primes
