import Re
import Automaten
import Data.List

toDEA :: Eq a => RegExp a -> EA Integer a
toDEA (Terminal x) = ([1,2], [x], [(1,[x],2)], [1], [2]) 
toDEA (Sequence r1 r2) = 
  let (q1, e1, s1, i1, f1) = (toDEA r1)
      (q2, e2, s2, i2, f2) = makeDisjunct (toDEA r1) (toDEA r2)
      sNew =((head f1, [], head i2) : union s1 s2)
  in (union q1 q2, union e1 e2, sNew, i1, f2)  
toDEA (Alternative r1 r2) =     
  let (q1, e1, s1, i1, f1) = (toDEA r1)
      (q2, e2, s2, i2, f2) = makeDisjunct (toDEA r1) (toDEA r2)
      iNew = maximum (union q1 q2) + 1
      fNew = iNew + 1
      qNew = union q1 q2 ++ [iNew, fNew]
      sNew = union s1 s2 ++ [(iNew, [], head i1), (iNew, [], head i2), 
                             (head f1, [], fNew), (head f2, [], fNew)] 
   in (qNew, union e1 e2, sNew, [iNew], [fNew])
toDEA (Repetition r) = 
  let (q, e, s, i, f) = toDEA r
      fNew = maximum q + 1
      sNew = s ++ [(head i, [], fNew), (head f, [], fNew), (fNew, [], head i)]
  in (q ++ [fNew], e, sNew, i, [fNew])



-- Return the second EA with all states renamed such that none has the same name
-- as a state in the first EA.
makeDisjunct :: EA Integer a -> EA Integer a -> EA Integer a
makeDisjunct (q1,_,_,_,_) (q2, e2, s2, i2, f2) = 
   let mapping = zip q2 (filter (\i -> notElem i q1) [1..])
       newState q = case lookup q mapping of 
                     Nothing -> error "unknown state"
                     Just qn -> qn
   in (map newState q2, e2, 
       map (\(p,a,q) -> (newState p, a, newState q)) s2,
       map newState i2, map newState f2)
      