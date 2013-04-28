

import Racers
import Game
import Parser

racer = simple
--race :: Racer -> Gamefield -> Gamestate

inOut :: String -> String -> String
inOut chks boosts = routeToStr route
   where gamefield = (parseCheckpoints chks, parseCheckpoints boosts)
         route = racer gamefield
    

main = do
         chks <- getLine
         boosts <-getLine
         putStrLn (inOut chks boosts)
         main


parseCheckpoints :: String -> [Checkpoint]
parseCheckpoints s = case result of 
      [(checkpoints,"")] -> checkpoints
      x -> error ("unexpected parse result: "++ show x)
   where result = norest (next3 (atom '[') tupelOrNothing (atom ']')
                             (\   _            x                    _  -> x)) s
         tupelOrNothing = alt (pKommaSep tupel) 
                              (convert (const []) (maybeSome (atom ' ')))
         tupel = next3 (atom '(') (pKommaSep parseNumber) (atom ')') convertints
         convertints _ is _ = listToCheckpoint (map fromIntegral is)
         listToCheckpoint [x1,y1,x2,y2,en] = ((x1,y1),(x2,y2),en)
         listToCheckpoint x = error ((show x) ++ ":unexpected format")

