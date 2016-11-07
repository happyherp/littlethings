import Utils(dividents)

triangles = map (\x->(x^2-x) `div` 2 )[2..]

   
result = find (\x -> length (dividents x) >500) trianglesv   

