import Prime8
import Utils
import Data.List


isPair p1 p2 =  let 
    dp1 = toDigits p1
    dp2 = toDigits p2
    in isPrime (fromDigits $ dp1++dp2) && isPrime (fromDigits $ dp2++dp1)


pairs = [[p2,p1]| p1 <- primes, 
                  p2 <- takeWhile (<p1) primes, 
                  isPair p1 p2]
                  
triplets = foldMap findNlets $ tail $ inits pairs
fourlets = foldMap findNlets $ tail $ inits triplets
fivelets = foldMap findNlets $ tail $ inits fourlets

findNlets nMinus1Groups = let
    lastM1Grp = last nMinus1Groups
    smallerPairs = takeWhile (\[_,b] -> b <= last lastM1Grp) pairs
    links = map head $ filter (\[_,b]-> b `elem` lastM1Grp) smallerPairs
    linksToAll = map head $ filter ((==length lastM1Grp) . length)$ group $ sort links
    in map (:lastM1Grp) linksToAll