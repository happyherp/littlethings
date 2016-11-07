
fib :: Int -> Int
fib 1 = 1
fib 2 = 2
fib x = fib (x-2) + fib (x-1)

below4M :: [Int]
below4M = takeWhile (< floor(4*10**6)) $ map fib [1..]

result :: Int
result = sum $ filter (\x-> (x `mod` 2) == 0) below4M