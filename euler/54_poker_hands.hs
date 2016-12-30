import Data.List
import Data.Maybe
import Data.Ord

type Hand = [Card]
data Color = S | C | H | D
    deriving (Read, Show, Eq)
    
data Card = Card Int Color deriving (Show)

number (Card n _) = n
color (Card _ c) = c

readCard :: String -> Card
readCard (n:(c:[])) = Card (readNumber n) (read (c:[])::Color)

readNumber n = case n `elemIndex` ['T','J','Q','K','A'] of 
    Just i   -> 10+i
    Nothing  -> read (n:[])::Int

twoHands :: String -> (Hand, Hand)
twoHands line = splitAt 5 $ map readCard $ words line 

readHands = do 
    content <- readFile "p054_poker.txt"
    return $ map twoHands (lines content)
    
p1Win :: Hand -> Hand -> Bool
p1Win a b = fullscore a > fullscore b    
    
fullscore :: Hand -> [Int]    
fullscore hand = mainscore hand ++ (reverse . sort . (map number)) hand
    
    
mainscore :: Hand -> [Int]    
mainscore hand = let 
    ranks = [highCard, (2 `ofAKind`), doublePair, (3 `ofAKind`),
             straight, flush, fullHouse, (4 `ofAKind`), 
             straightFlush, royalFlush]    
    in last $ map (\(v,add)->v:fromJust add) 
                  (filter (isJust.snd) $ zip [0..] (map (\f -> f hand) ranks))
    

type HandCheck = Hand -> Maybe [Int]    
    
royalFlush :: HandCheck    
royalFlush hand = if isJust (straightFlush hand) && hasAss then Just[] else Nothing
    where hasAss = maximum (map number hand) == 14
    
straightFlush :: HandCheck    
straightFlush hand = if isJust (flush hand) && isJust (straight hand) 
    then Just [] else Nothing
    
ofAKind n hand= listToMaybe $ map ( (:[]) . head) $ filter ((==n).length) $ group $ sort $ map number hand         
    
doublePair :: HandCheck    
doublePair hand = let 
    pairs = filter ((==2).length) $ group $ sort $ map number hand         
    in if length pairs == 2 then Just $ reverse $ sort $ map head pairs else Nothing

fullHouse :: HandCheck    
fullHouse hand = case (2 `ofAKind` hand, 3 `ofAKind` hand) of 
    (Just v2, Just v3) -> Just (v3++v2)
    _ -> Nothing

flush :: HandCheck    
flush hand = let
    (c:rest) = map color hand
    in if all (== c) rest then Just [] else Nothing
    
straight :: HandCheck    
straight hand = if incByOne $ sort $ map number hand then Just [] else Nothing
  where incByOne (a:b:rest) = b-a == 1 && incByOne (b:rest)
        incByOne _ = True
  
highCard :: HandCheck  
highCard _ = Just []    
    
result = do 
   hands <- readHands
   return $ length $ filter (uncurry p1Win) hands
   
test s1 s2 = p1Win (map readCard $ words s1) (map readCard $ words s2)