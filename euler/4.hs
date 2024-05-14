isPalin x = let 
            str = show x
            in str == reverse str

range = [100..999]

result =  maximum $ filter isPalin [x*y| x <- range, y <-range]