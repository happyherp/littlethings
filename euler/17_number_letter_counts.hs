

zeroToNineteen = [
    "zero", "one", "two", "three", "four", "five", 
    "six", "seven", "eight", "nine", "ten", "eleven",
    "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
    "seventeen", "eighteen", "nineteen"] 
    
tenners = [
    "twenty", "thirty", "forty", "fifty", 
    "sixty", "seventy", "eighty", "ninety"]    

inWords :: Int -> String
inWords x | x <= 19 = zeroToNineteen !! x
          | x < 100 = let (firstDigit, secondDigit) = x `divMod` 10
                          firstWord = tenners !! (firstDigit-2)
                          secondWord = inWords secondDigit
                      in firstWord ++ 
                        if secondDigit == 0 then "" else "-" ++ secondWord
          | x < 1000 = let (hundreds, rest) = x `divMod` 100
                       in (inWords hundreds) ++" hundred" ++
                             if rest == 0 then "" else " and "++(inWords rest)
          | x == 1000 = "one thousand"
          
          
countLetters :: Int -> Int
countLetters x = length $ filter (flip elem ['a'..'z']) (inWords x)
          
result = sum $ map (countLetters) [1..1000]         