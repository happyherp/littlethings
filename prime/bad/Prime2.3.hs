isPrime 1 = False
isPrime x = x > 0 && not (any divisible possibleDivisors)
    where divisible y = x `mod` y == 0
          possibleDivisors = takeWhile (<x) primes

primes = filter isPrime [1..]