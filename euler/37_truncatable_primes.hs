import Prime2(isPrime, primes)
import Utils(fromDigits, toDigits)

isTruncatable :: Integer -> Bool
isTruncatable n | n < 10 = False
                | otherwise = let
    digits = toDigits n
    fromLeft  = map (\i ->fromDigits(drop i digits)) [1..(length digits)-1]
    fromRight = map (\i ->fromDigits(take i digits)) [1..(length digits)-1]
    in all isPrime (n:fromLeft++fromRight)

    
truncabale_primes = filter isTruncatable (take (10^5) primes)

result = if (length truncabale_primes) == 11 then sum truncabale_primes
            else error "Must be 11"