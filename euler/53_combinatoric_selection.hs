fak n = product [1..n]

c n r = fak n `div` (fak r *  fak (n - r))

over1M n = takeWhile (> 10^6) $ dropWhile (<= 10^6) $ map (c n) [1..n]

result = sum $ map (length . over1M) [1..100]