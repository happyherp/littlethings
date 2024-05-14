target = 1000
     
triplets :: [(Integer, Integer, Integer)]     
triplets = [(a,b,c)|c <- [3..target],
                     b <- [2..(c-1)],
                     let a = target - b - c,
                     a < b,
                     a >= 1,
                     a^2 + b^2 == c^2]
       
result :: Integer       
result =  let (a,b,c) = (head triplets) 
          in a*b*c