import Prime8
import Utils
import Data.List
import Data.Function(on)
import qualified Data.Map as M
import System.TimeIt
import Control.Parallel.Strategies 


isPair p1 p2 =  let 
    dp1 = toDigits p1
    dp2 = toDigits p2
    in isPrimeSparse (fromDigits $ dp1++dp2) && isPrimeSparse (fromDigits $ dp2++dp1)


pairs = [(p2,p1)| p1 <- primes, 
                  p2 <- takeWhile (<p1) primes, 
                  isPair p1 p2]
           
           
--other apprach took to long. trying something else.            

graphMap = M.fromList $  parMap rpar (\grp->(fst $ head grp,map snd grp)) 
                                         $ groupBy ((==) `on` fst) $ sort $ take (2*10^4) pairs 
                       
   
partners :: Integer -> [Integer]   
partners p = case M.lookup p graphMap of 
    Just partners -> partners
    Nothing ->  []

                       
groupsOf :: Integer -> Integer -> [[Integer]]
groupsOf 2 p =  case M.lookup p graphMap of 
    Just others -> map (\o->[p,o]) others
    Nothing -> []
    
groupsOf n p = let 
    smallerGroups = groupsOf (n-1) p     
    in foldMap extendGroup smallerGroups
    
extendGroup :: [Integer] -> [[Integer]]    
extendGroup primegroup = 
    map ((primegroup++).(:[]).head) 
        $ filter ((== length primegroup).length) 
               $ group $ sort $ foldMap partners primegroup

    
result =  head $ filter (not . null) $ foldMap (groupsOf 4  ) $ map fst $ M.toList graphMap    
    
main = timeIt $ print result   
    
