
sqrtFraction ::   Float -> [Float]
sqrtFraction n = let 
    a1 = fromIntegral $ floor $ sqrt n
    f x = (n-a1**2)/(2*a1+x)
    in map (+ a1) $ iterate f 0