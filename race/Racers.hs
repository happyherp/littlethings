
module Racers where 

import Game

import Debug.Trace

{- A very simple racer. Goes to all checkpoints in order by drifting. -}
simple :: Racer
simple field = snd (simple' (startstate field))

simple' :: Gamestate -> (Gamestate, [Direction])
simple' (car, field, Driving) = (state2, directions1 ++ directions2)
    where nextChkPnt = (head . fst) field
          directions1 = driftTo car nextChkPnt
          state1 = foldl step (car, field, Driving) directions1
          (state2, directions2) = simple' state1
simple' endstate = (endstate, [])


--Use a single acceleration into each direction, then dirft until the target is reached and stop.
driftTo :: Car -> Checkpoint -> [Direction]

driftTo car chkpoint = (driftToG gx car chkpoint) ++ (driftToG gy car chkpoint)

driftToG :: (Pos -> Int) -> Car -> Checkpoint -> [Direction]
driftToG g (carpos,_,_) chkpoint | doesHitG g carpos chkpoint = []
                                 | otherwise = [push]++drift++[stop]
            where mindist = mindistG g  carpos chkpoint
                  drift = take ((abs mindist) - 1) (repeat (0,0))
                  (dirp, dirm) = (getDir g 1, getDir g (-1)) 
                  (push, stop) = if mindist > 0 then (dirp, dirm) else (dirm, dirp)


mindistG :: (Pos -> Int) -> Pos -> Checkpoint -> Int
mindistG g spos (pos1,pos2,_) = dist
           where dist = minAbs (g (distPos spos pos1)) (g (distPos spos pos2))
                 minAbs a b = if abs a < abs b then a else b 




