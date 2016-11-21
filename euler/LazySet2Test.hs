
import LazySet2(fromList, member)  
        
import qualified Data.List.Ordered as OList
import qualified Data.List as List
import qualified Data.Set as NSet
          
infList = filter even [1..]    
infSet = fromList infList
normalSet = NSet.fromList $ take (startpoint+elementCount) infList


startpoint = 10^6
elementCount = 10^3

doTest f = length $ filter f [startpoint..startpoint+elementCount]

withList = doTest $ \i-> OList.member i infList
withNormalSet = doTest  $ \i-> NSet.member i normalSet
withInfSet = doTest $ \i-> member i infSet

runAll = print [withInfSet, withList, withNormalSet]
main = print withInfSet
