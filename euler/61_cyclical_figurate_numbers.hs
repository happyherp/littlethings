
import Data.List

p 3 n = n*(n+1) `div` 2
p 4 n = n*n
p 5 n = n*(3*n-1) `div` 2
p 6 n = n*(2*n-1)
p 7 n = n*(5*n-3) `div` 2
p 8 n = n*(3*n-2)

groups = map getFours [3..8]


fourDigit :: [Int]
fourDigit = nub $ concat groups 
                       
getFours d = takeWhile (<= 9999) $ 
             dropWhile (< 1000)  $ 
             map (p d) [1..]                       
                       
starts = sort $ nub $ map (`div` 100) fourDigit
ends =   sort $ nub $ map (`mod` 100) fourDigit  

buildCycles :: [Int] -> [Int] -> Int -> [[Int]]
buildCycles _     _         0 = []
buildCycles []    remaining n = let
    recurse candidate = buildCycles [candidate] (delete candidate remaining) (n-1)
    in foldMap recurse remaining 

buildCycles sofar remaining 1 = let 
    first = head sofar `div` 100
    lastTwo =  last sofar `mod` 100 
    matches n = n `mod` 100 == first && n `div` 100 == lastTwo
    candidates = filter matches remaining
    in map ((sofar ++).(:[])) candidates
    
buildCycles sofar remaining n = let
    lastTwo =  last sofar `mod` 100
    candidates = filter ((==lastTwo) . (`div` 100)) remaining
    recurse candidate = buildCycles (sofar ++ [candidate]) 
                                    (delete candidate remaining)
                                    (n-1)
    in foldMap recurse candidates
    
    
    
cycles = nub $ map sort $ buildCycles [] fourDigit 6

hasOneOfEveryClass cycle = all hasOne groups
    where hasOne group = any (`elem` group) cycle
    
    

oneToOne :: Ord b => [(a,[b])] -> [[(a,b)]]
oneToOne m = oneToOne' $ map (\(key, vals) -> (key, nub $ sort vals)) m
    where oneToOne' :: Ord b => [(a,[b])] -> [[(a,b)]]
          oneToOne' [] = []
          oneToOne' ((k,vals):[]) = map (\v->[(k,v)]) vals
          oneToOne' ((k,vals):cs) = foldMap tryOne vals
             where tryOne val = map ((k,val):) (oneToOne' withoutVal)
                    where withoutVal = map (\(key, vals) -> (key, delete val vals)) cs

                    
oneClassToOneNum cycle = let 
    mapping = map (\num-> (num,numToClass num)) cycle
    numToClass num = filter (num `elem`) groups     
    in  not $ null $ oneToOne mapping 

result = map sum $ filter oneClassToOneNum cycles

