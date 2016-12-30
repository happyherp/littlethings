import Data.List.Ordered(minus, mergeAll)

--Returns an infinite List of all multiples of n except n itself
multiplesOf n = iterate (+n) (2*n)
--Infinite List of the Lists of multiples of all positive numbers.
multiples     = map multiplesOf [2..]
--Multiples of all numbers merged together into a single ordered list.
composites    = mergeAll multiples
--List of prime numbers by removing the composites from the set of all numbers.
primes        = [2..] `minus` composites

main = print $ sum $ takeWhile (< 10^5) primes