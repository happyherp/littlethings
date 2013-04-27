-- Für den Prog-wettbewerb der FH.

import Data.List (partition)

data Direction = N | E | S | W | D


type Pos = (Int,Int)

type Gamefield = ([Checkpoint],[Booster])
type Checkpoint = (Pos,Pos,Int) --x1,y1,x2,y2,points
type Booster = Checkpoint -- They look the same actually.

type Car = (Pos,Pos,Int) --xpos,ypox,xspeed,yspeed,energy

type Gamestate = (Car,Gamefield, Win) 

data Win = Drive | Won Int | LostNoGas
  deriving Show 

--A racer is a function able of creating Directions for a given gamefield.
--Diffrent Racing-Strategies will be implemented that way.
type Racer = Gamefield -> [Direction]


--Let the racer race in the gamefield. Return an int, if he makes it to the end.
race :: Racer -> Gamefield -> Win
race racer field = won
   where route = racer field
         (car, field, won) = runRoute route field


runRoute :: [Direction] -> Gamefield -> Gamestate
runRoute route field = foldl step startstate route
   where ((fstChk:rstChkPnts),bstrs) = field
         startstate = case fstChk of  
            ((x1,y1),(x2,y2),en)
                -> let xpos = (x1+x2) `div` 2
                       ypos = (y1+y2) `div` 2
                   in (((xpos,ypos),(0,0),en),(rstChkPnts,bstrs), Drive)


step :: Gamestate -> Direction -> Gamestate
step (car, field, won) dir = (tankedcar, newfield, newwon)
   where movedcar = move car dir
         (tankedcar, newfield) = checkFields movedcar field
         newwon = case won of 
           Drive -> case newfield of 
             ([],_) -> case tankedcar of (_,_,en) -> Won en
             x -> case tankedcar of 
                  (_,_,0) -> LostNoGas            
                  x -> Drive
           x -> x
            

move :: Car -> Direction -> Car
move (pos,speed,en) dir = (addPos pos newspeed, newspeed, en-1)
   where newspeed = addPos speed (case dir of
                      N -> ( 0,-1)
                      E -> ( 1, 0)
                      S -> ( 0, 1)
                      W -> (-1, 0)
                      D -> ( 0, 0))
         
checkFields :: Car -> Gamefield -> (Car,Gamefield)
checkFields (p,s,en) (checkpoints, boosters) = 
     ((p,s,newen) ,(unhitCp,unhitBst))
   where (hitCp,unhitCp)   = partition (doesHit p) checkpoints
         (hitBst,unhitBst) = partition (doesHit p) boosters
         newen = foldr (\(_,_,e) a-> e+a) en (hitCp++hitBst)
         

doesHit :: Pos -> Checkpoint -> Bool
doesHit (x,y) ((cpx1,cpy1),(cpx2,cpy2),_) = 
   cpx1 <= x && x <= cpx2 && cpy1 <= y && y <= cpy2


strToRoute :: String -> [Direction]
strToRoute = map (\c -> case c of 
   'N' -> N 
   'E' -> E
   'S' -> S
   'W' -> W
   'D' -> D)


addPos :: Pos -> Pos -> Pos
addPos (x1,y1) (x2,y2) = (x1+x2,y1+y2)
