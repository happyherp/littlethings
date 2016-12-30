
isPrime x = x > 0 && filter divisible [1..x] == [1,x]
    where divisible y = x `mod` y == 0

primes = filter isPrime [1..]