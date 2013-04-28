-- Für den Prog-wettbewerb der FH.
module Game where 

import Data.List (partition)

type Pos = (Int,Int)

type Direction = Pos
directions =     [(0,0),(0,-1),(1,0),(0,1),(-1,0)]
directionnames = [ 'D' , 'N'  , 'E' , 'S' ,  'W' ]

type Gamefield = ([Checkpoint],[Booster])
type Checkpoint = (Pos,Pos,Int) --x1,y1,x2,y2,points
type Booster = Checkpoint -- They look the same actually.

type Car = (Pos,Pos,Int) --pos,speed,energy

type Gamestate = (Car,Gamefield, Winstate) 

data Winstate = Driving | Won Int | NoGas
  deriving (Show,Eq)

--A racer is a function able of creating Directions for a given gamefield.
--Diffrent Racing-Strategies will be implemented that way.
type Racer = Gamefield -> [Direction]



---- Handling Positions --------
addPos :: Pos -> Pos -> Pos
addPos (x1,y1) (x2,y2) = (x1+x2,y1+y2)

multPos :: Pos -> Int -> Pos
multPos (x,y) f =(f*x,f*y) 

distPos :: Pos -> Pos -> Pos
distPos p1 p2 = p2 `addPos` (p1 `multPos` (-1))

absPos :: Pos -> Pos
absPos (x,y) = (abs x, abs y)

--Acessors for positions. Useful to reuse code in diffrent dimensions.
gx :: Pos -> Int
gx = fst
gy :: Pos -> Int
gy = snd
getDir :: (Pos->Int) -> Int -> Pos --Reverse lookup. Get the Position,from the accessor.
getDir g i = head (filter ((== i) . g) directions)

--Let the racer race in the gamefield. Return Winstate
race :: Racer -> Gamefield -> Gamestate
race racer field = runRoute route field
   where route = racer field

runRoute :: [Direction] -> Gamefield -> Gamestate
runRoute route field = foldl step (startstate field) route

startstate :: Gamefield -> Gamestate
startstate ((fstChk:rstChkPnts),bstrs) = case fstChk of  
      ((x1,y1),(x2,y2),en) -> let xpos = (x1+x2) `div` 2
                                  ypos = (y1+y2) `div` 2
                               in (((xpos,ypos),(0,0),en),
                                   (rstChkPnts,bstrs), 
                                   Driving)

step :: Gamestate -> Direction -> Gamestate
step (car, field, won) dir = (tankedcar, newfield, newwon)
   where movedcar = move car dir
         (tankedcar, newfield) = checkFields movedcar field
         newwon = case won of 
           Driving -> case newfield of 
             ([],_) -> case tankedcar of (_,_,en) -> Won en
             x -> case tankedcar of 
                  (_,_,0) -> NoGas            
                  x -> Driving
           x -> x

move :: Car -> Direction -> Car
move (pos,speed,en) dir = (addPos pos newspeed, newspeed, en-1)
   where newspeed = addPos speed dir
         
checkFields :: Car -> Gamefield -> (Car,Gamefield)
checkFields (p,s,en) (checkpoints, boosters) = 
     ((p,s,newen) ,(unhitCp,unhitBst))
   where (hitCp,unhitCp)   = partition (doesHit p) checkpoints
         (hitBst,unhitBst) = partition (doesHit p) boosters
         newen = foldr (\(_,_,e) a-> e+a) en (hitCp++hitBst)
         

doesHit :: Pos -> Checkpoint -> Bool
doesHit pos chk = (doesHitG gx pos chk) && (doesHitG gy pos chk)

doesHitG :: (Pos->Int) -> Pos -> Checkpoint -> Bool 
doesHitG g carpos (chkpos1,chkpos2,_) = g chkpos1 <= g carpos && g carpos <= g chkpos2


mindist :: Pos -> Checkpoint -> Pos
mindist spos chkpt = (mindistG gx spos chkpt, mindistG gy spos chkpt)

mindistG :: (Pos -> Int) -> Pos -> Checkpoint -> Int
mindistG g spos (pos1,pos2,_) = dist
           where dist = minAbs (g (distPos spos pos1)) (g (distPos spos pos2))
                 minAbs a b = if abs a < abs b then a else b 



