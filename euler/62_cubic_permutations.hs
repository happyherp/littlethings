import Utils(toDigits, keep)
import Data.Ord(comparing)
import Data.List
import Data.Function(on)

cubes = map (^3) [1..]

digitCubes n = dropWhile (< 10^(n-1)) $
               takeWhile (< 10^n) cubes

permGroups cubeGrp = map (\group -> (minimum $ map fst group, length group)) $ 
                     groupBy ((==) `on` snd) $ 
                     sortBy  (comparing snd) $ 
                     map (keep (sort . toDigits)) cubeGrp
                     
                     
allpermGroups = foldMap (permGroups . digitCubes) [1..]
                     
                     
withCount n permGroup = snd permGroup >= n

result = head $ filter (withCount 5) allpermGroups