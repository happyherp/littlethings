import Utils(permutate)
import Data.List(nub)

allPerms = permutate [1..9]

toNum::[Int] -> Int
toNum is = read $ concat $ map show is

isPandigital :: ([Int],[Int],[Int]) -> Bool
isPandigital (a,b,c) = (toNum a) * (toNum b) == (toNum c)

var1 :: [Int] -> ([Int],[Int],[Int])
var1 nums = (take 1 nums, take 4 (drop 1 nums), drop 5 nums)

var2 :: [Int] -> ([Int],[Int],[Int])
var2 nums = (take 2 nums, take 3 (drop 2 nums), drop 5 nums)

pan_products =  filter isPandigital $    (map (var1) allPerms) 
                                      ++ (map (var2) allPerms)

result = sum $ nub $ map (\(_,_,p)->toNum p) pan_products
