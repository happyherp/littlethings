import Data.Char(digitToInt)
import Data.Maybe(fromJust)
import Data.List(find)

powers n = map (^n) [0..9]

digitpower n x = sum $ map ((^n) . digitToInt) $ show x 

isPowerSum n x = (digitpower n x) == x

highest n = let niners = map read $ iterate ('9':) "9"
                alwaysToSmall x = digitpower n x < x
            in fromJust $ find alwaysToSmall niners       
            
powerDigits n = filter (isPowerSum n) [2..highest n]            

result = sum $ powerDigits 5