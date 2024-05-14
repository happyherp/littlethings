

import Racers
import Game
import IOUtils

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


