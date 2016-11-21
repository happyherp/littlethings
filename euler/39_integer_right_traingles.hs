import Data.Maybe(isJust, fromJust)
import Data.List(maximumBy)
import Data.Ord(comparing)
import Utils(keep)

result = maximumBy (comparing (length . possibleTriangles)) [1..1000]

check (a,b,c,p) | a^2 + b^2 /= c^2 = error "a and b did not match c"
                | a+b+c /= p = error "Sum of sides did not match perimeter"
                | otherwise = (a,b,c,p)

possibleTriangles p = 
   map (fromJust) $ filter isJust $ map (getIntegerTriangle p) [1..(p-1)]

getIntegerTriangle p b = do
  a <- getA b p
  c <- intSqrt (a^2+b^2)
  return (check (a,b,c,p))

getA b p = let 
  (a, rest) = (p^2 - 2*p*b ) `divMod` (2*p-2*b)
  in if 0< a && a <= b && rest == 0 then Just a else Nothing
  
  
intSqrt :: Integral a => a -> Maybe a
intSqrt x = let 
  root = (sqrt $ fromIntegral x)
  noLeftOver = fromIntegral (floor(root)) == root
  in if noLeftOver then Just (floor root) else Nothing
  
  
--Old version
isIntegerTriangle3 :: Integral a => a -> a -> a -> Bool
isIntegerTriangle3 a b p = let
  c = floor (sqrt $ fromIntegral ((a^2) + (b^2)))
  in a + b + c == p  