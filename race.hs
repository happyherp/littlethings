-- Für den Prog-wettbewerb der FH.


data Direction = N | E | S | W | D


type Gamefield = ([Checkpoint],[Booster])
type Checkpoint = (Int,Int,Int,Int,Int) --x1,y1,x2,y2,points
type Booster = Checkpoint -- They look the same actually.

type Car = (Int,Int,Int,Int,Int) --xpos,ypox,xspeed,yspeed,energy

--A racer is a function able of creating Directions for a given gamefield.
--Diffrent Racing-Strategies will be implemented that way.
type Racer = Gamefield -> [Direction]


--Let the racer race in the gamefield. Return an int, if he makes it to the end.
race :: Racer -> Gamefield -> Maybe Int
race racer field = 
   where route = racer field


step :: (Car,GameField) -> Direction -> (Car, GameField)
step (car, field) dir = (tankedcar, newfield)
   where movedcar = applyDirection car dir
         (tankedcar, newfield) = checkFields movedcar field

applyDirection :: Car -> Direction -> Car
applyDirection (x,y,xs,ys,en) dir = move case dir of 
     N -> (x,y-1,xs,ys,en)
     E -> (x+1,y,xs,ys,en)
     S -> (x,y+1,xs,ys,en)
     W -> (x-1,y,xs,ys,en)
     D -> (x,  y,xs,ys,en)

move :: Car -> Car
move (x,y,xs,ys,en) = (x+xs,y+ys,xs,ys,en-1)



checkFields :: Car -> Gamefield -> (Car,Gamefield)
checkFields (x,y,xs,ys,en) (checkpoints, boosters) = 
     ((x,y,xs,ys,newen) ,(unhitCp,unhitbst))
   where (hitCp,unhitCp)   = partition (doesHit car) checkpoints
         (hitBst,unhitBst) = partition (doesHit car) boosters
         newen = foldr (\(_,_,_,_,e) o -> e+o) en (hitCp++hitBst)
         

doesHit :: Car -> Checkpoint -> Bool
doesHit (carx,cary,_,_,__) (cpx1,cpy1,cpx2,cpx2,_) = 
   cpx1 <= carx && cary <= cpx2 && cpy1 <= cary && cary <= cpy2
