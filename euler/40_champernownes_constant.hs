import Utils(toDigits)


fractional =  foldMap toDigits [1..]

d n = fractional !! (n-1)

result = d 1 * d 10 * d 100 * d 1000 * d 10000 * d (10^5) * d (10^6)