import Prime8
import Data.List
import Data.Ord

limit = 10^6

primeSeqs xs = primeSumSeqs
    where seqsBelowM = takeWhile ( (< limit) . sum) $ inits xs
          primeSumSeqs = filter (isPrime . sum) seqsBelowM

relevantSeqs = tails $ takeWhile (<= limit `div` 5) primes         

result = sum $  maximumBy (comparing length) $ foldMap primeSeqs relevantSeqs

main = print result