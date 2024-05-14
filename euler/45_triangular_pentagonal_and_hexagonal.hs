
import Utils(merge)

t n = n*(n+1)   `div` 2
p n = n*(3*n-1) `div` 2
h n = n*(2*n-1) 


merged = foldl1 merge $ map (\f->map f [1..]) [t,p,h]


tripplenums (n1:n2:n3:xs) | n1 == n2 && n1 == n3 = n1:(tripplenums xs)
                          | otherwise = tripplenums (n2:n3:xs)
                          
                          
result = drop 2 (tripplenums merged)