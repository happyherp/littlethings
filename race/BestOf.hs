

import Data.Time.Clock
import Control.DeepSeq




bestOfNextSeconds :: (Ord a, NFData a) => DiffTime -> [a] -> IO (a, [a])
bestOfNextSeconds nextseconds (x:xs) = do 
   now <- getCurrentTime
   putStrLn "bestOfNextSeconds"
   let stopAt = case now of UTCTime day secs -> UTCTime day (secs+nextseconds) 
      in bestUntil stopAt x xs


bestUntil :: (Ord a, NFData a) => UTCTime -> a -> [a] -> IO (a, [a])
bestUntil stopat accum [] = do return (accum, [])
bestUntil stopat accum (next:rest) = 
   do nextmax <- return (max next accum)
      now <- deepseq next getCurrentTime
      if now >= stopat then return (nextmax, rest)
      else bestUntil stopat nextmax rest



fac 1 = 1
fac x = x * fac (x-1)
longthing = (map fac [1..]::[Int])

testSecond = do r <- bestOfNextSeconds 1 longthing
                return (fst r)

{-
testInterval = do 
   result <- bestPerInterval longthing
   return (take 2 result)
-}



