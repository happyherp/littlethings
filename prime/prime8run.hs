
import Prime8
import Data.Time(getCurrentTime,diffUTCTime)
import System.Environment(getArgs)



--main = print $ sum $ take (10^5) primes
main = do
     args <- getArgs
     let limit = case args of 
                       [x] -> read x :: Integer
                       _ -> 10^5
     print $ "limit is "++(show limit)
     start <- getCurrentTime                       
     print $ sum $ takeWhile (<limit) primes
     end <- getCurrentTime
     print ("took "++ (show (diffUTCTime end start)))