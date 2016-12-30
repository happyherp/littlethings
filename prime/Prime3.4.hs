isPrime 1 = False
isPrime 2 = True
isPrime x = x > 0 && not (any divisible possibleDivisors)
    where divisible y = x `mod` y == 0
          highestPossibileDivisor = ceiling $ sqrt $ fromIntegral x
          possibleDivisors = map snd $ filter fst $ takeWhile ( \(_,n) -> n <= highestPossibileDivisor) primeCache

          
primeCache = zip (map isPrime [1..]) [1..]
          
primes = map snd $ filter fst $ primeCache 