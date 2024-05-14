import Data.List(find, sort)
import Utils

isPermuted n = all (== sort digits) $ map (sort . toDigits . (*n))  [2,3,4,5,6]
    where digits = toDigits n


result = find isPermuted [1..]