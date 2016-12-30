
isPrime 1 = False
isPrime 2 = True
isPrime x = x > 0 && not (any divisible possibleDivisors)
    where divisible y = x `mod` y == 0
          highestPossibileDivisor = ceiling $ sqrt $ fromIntegral x
          possibleDivisors = takeWhile (<= highestPossibileDivisor) primes

primes = filter isPrime [1..]