
module IOUtils where

import Game
import Parser


routeToStr :: [Direction] -> String
routeToStr dirs = map (\dir -> lookupE dir (zip directions directionnames)) dirs

strToRoute :: String -> [Direction]
strToRoute s = map (\c -> lookupE c (zip directionnames directions)) s

lookupE elem assoclist= case lookup elem assoclist of 
        Just c -> c
        Nothing -> error "Element not found"


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
