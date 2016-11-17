
isBase10Palindrome x =  let
   asText = show x
   in  asText == reverse asText
   

toBinString :: Int -> String
toBinString x = let
  (div, rest) = x `divMod` 2
  curDigit =  case rest of  0 -> "0"; 1 -> "1"
  prevPart = if div /= 0 then toBinString div else "" 
  in prevPart ++ curDigit
  
isBase2Palindrome x = let 
   asText = toBinString x   
   in  asText == reverse asText
   
   
doublePalindromes = filter (\x -> isBase10Palindrome x && isBase2Palindrome x )[1..10^6]   

result = sum doublePalindromes