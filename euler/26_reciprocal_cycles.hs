import Data.List(elemIndex, maximumBy)
import Utils(keep)
import Data.Ord(comparing)

recicycle :: Int -> [Int]
recicycle d = descend [1] [0]
    where descend rs ds = let (a, rest) = (head (rs) * 10) `divMod` d
                              divisors =  (a:ds)
                          in if (rest == 0) then [] 
                             else case elemIndex rest rs of
                                 Just i  -> reverse $ take (i+1) divisors
                                 Nothing -> descend (rest:rs) divisors
                                 
result = fst $ maximumBy (comparing (length . snd)) $ map (keep recicycle) [1..(1000-1)]