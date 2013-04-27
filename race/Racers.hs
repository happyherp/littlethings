
module Racers where 

import Game

{- A very simple racer. Goes to all checkpoints in order by drifting. -}
simple :: Racer
simple field = snd (simple' (startstate field))

simple' :: Gamestate -> (Gamestate, [Direction])
simple' (car, field, Drive) = (state2, directions1 ++ directions2)
    where nextChkPnt = (head . fst) field
          directions1 = driftTo car nextChkPnt
          state1 = foldl step (car, field, Drive) directions1
          (state2, directions2) = simple' state1
simple' endstate = (endstate, [])


--Use a single acceleration into each direction, then dirft until the target isreached. Then stop.
driftTo :: Car -> Checkpoint -> [Direction]
driftTo car chkpoint = (driftToG gx car chkpoint) ++ (driftToG gy car chkpoint)

driftToG :: (Pos -> Int) -> Car -> Checkpoint -> [Direction]
driftToG g (carpos,_,_) chkpoint | doesHitG g carpos chkpoint = []
                                 | otherwise = [push]++drift++[stop]
            where (pos1, pos2,_) = chkpoint
                  mindist = minimum (map ((g carpos -).g) [pos1,pos2]) 
                  drift = take ((abs mindist) - 1) (repeat (0,0))
                  (dirp, dirm)  = (getDir g 1, getDir g (-1)) 
                  (push, stop) = if mindist < 0 then (dirp, dirm) else (dirm, dirp)

field = ([((10,10),(20,20),40), ((50,19),(60,20),40)],[((30,30),(40,40),100)]) :: Gamefield

