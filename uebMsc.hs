
qsort   :: Ord a => [a] -> [a]
qsort []
    = []
 
qsort (x : xs)
    = qsort lessX ++ [x] ++ qsort greaterX
    where
    (lessX, greaterX) = divide x xs



--aufgabe: lessX und greaterX erzeugen ohne zweimal durch das array zu gehen. 

divide :: Ord a => a -> [a] -> ([a],[a])
divide pivot [] = ([],[])
divide pivot (x:xs) = if x <= pivot then (x:l,h) else (l,x:h)
        where (l,h) = divide pivot xs
