import Data.List(null, find)
import Data.List.Ordered(minus)

--Find all primes smaller or equal to x. 
primesUntil x = snd $ loopWhile (not . null . fst) strikeOutNextPrime ([2..x],[])


-- takes the first un-outstriked number and adds it to the list of primes. Strikes out all multipes of that prime. 
strikeOutNextPrime (free, primes) = (free `minus` mults, primes ++ [newPrime])
    where newPrime = head free
          mults = iterate (+newPrime) newPrime
          
--Simple loop-function. Applies to body to state while cond applies to the state.
loopWhile cond body state | cond state = loopWhile cond body (body state)
                          | otherwise = state