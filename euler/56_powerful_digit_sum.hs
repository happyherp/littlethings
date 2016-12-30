import Utils

powers = [a^b|a <- [1..99], b <- [1..99]]

result =maximum $  map (sum . toDigits) powers
