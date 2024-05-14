import Data.Bits
import Data.Char
import Utils
import Data.List
import Data.Ord

encoded = do 
    content <- readFile "p059_cipher.txt"
    return $ (read::String ->[Int]) ( "["++ content ++"]")
    
    
    
some_english_words = [
    "and", "I", "do", "so", "he", "she", "it", "the", "is", "was"]    
    
    
toString :: [Int] -> String
toString = map chr
    
toInts :: String -> [Int]
toInts = map ord    
    
applyKey :: [Int] -> [Int] -> [Int]
applyKey text key = map (\(c,k) -> c `xor` k) $ zip text (cycle key)


keyrange = ['a'..'z']

possible_keys = [toInts (a:b:c:[])| a<-keyrange, b<-keyrange, c<-keyrange]

englishness text = sum $ map ((count text) . toInts) some_english_words

count s t = sum [ 1 | r <- tails s, isPrefixOf t r ]

main = do    
    s <- encoded 
    let possibletexts = map (\k->applyKey s k) possible_keys
    let withScore = map (keep englishness) possibletexts
    let text = toString $ fst $ maximumBy (comparing snd) withScore
    print text                       
    return $ sum $ toInts text