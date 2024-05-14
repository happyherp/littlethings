britcoins :: [Int]
britcoins = [200,100,50,20,10,5,2,1]

sumCoin (m,c) = m*c

buildSum :: Int->[Int]->[[(Int,Int)]]
buildSum 0 _  = [ [] ]
buildSum _ [] = []
buildSum sum (coin:coins) = 
   let withThisCoin = takeWhile ((sum >=) . sumCoin)  [(m, coin)|m<-[0..]]
       addRest mc = map (mc:) (buildSum (sum - (sumCoin mc)) coins)
   in concat $ map addRest withThisCoin

result = length $ buildSum 200 britcoins