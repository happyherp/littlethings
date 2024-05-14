import Utils(quersumme)

fac 1 = 1
fac n = n* fac(n-1)

result = quersumme $ fac 100