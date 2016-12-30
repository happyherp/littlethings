import Utils(toDigits)

expand 0 = (2,1)
expand n = let (a,b) = expand (n-1)
           in (b+2*a,a)
           
sqrt2 n = let (a,b)  = expand (n) in (b+a, a)

moreNumDigits (n,d) = length (toDigits n) > length (toDigits d)

result = length $ filter moreNumDigits $ map sqrt2 [1..1000]