{-Run a set of maps with a racer to see how well he does-}

module Testmaps where

import System.Directory
import System.IO
import Game
import Racers
import IOUtils

racer = simple

main = do files <- getDirectoryContents "./maps"
          results <- mapM (runRacerOnFile racer) (onlyfiles files)
          putStrLn (display results)
   where onlyfiles = (map ("./maps/"++))
                      .
                     (filter (\f ->f /= "." && f /= ".." && (last f) /= '~' ))

runRacerOnFile :: Racer -> FilePath -> IO (FilePath, Gamestate)
runRacerOnFile racer filepath =  do 
   fileh <- openFile filepath ReadMode
   line1 <- hGetLine fileh
   line2 <- hGetLine fileh
   return (let checkpoints = parseCheckpoints line1
               boosters = parseCheckpoints line2 
           in (filepath, race racer (checkpoints,boosters)))



display :: [(FilePath, Gamestate)] -> String
display = (foldl join "") . (map displayline)
  where displayline x = case x of 
           (filepath, (_,_,winstate)) -> filepath ++ "  Result: "++ show winstate
        join c x  = c++x++"\n"

                           
