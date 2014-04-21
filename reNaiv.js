
import Data.Set (Set, singleton, empty, union, unions, member, insert, delete)
import qualified Data.Set as Set
import Test.HUnit


data RegExp a = Terminal a 
               | Sequence (RegExp a) (RegExp a)
               | Alternative (RegExp a) (RegExp a)
               | Repetition (RegExp a) 
   deriving Show
   
matches :: Ord a => (RegExp a) -> [a] -> Bool
matches r s =  member [] (matchPart r s)

matchPart ::  Ord a => (RegExp a) -> [a] -> Set [a]
matchPart (Terminal t) (x:xs) | t == x = singleton xs
matchPart (Terminal _) _ = empty
matchPart (Sequence r1 r2) xs = unions $ map (matchPart r2) 
                                             (Set.toList (matchPart r1 xs))
matchPart (Alternative r1 r2) xs = union (matchPart r1 xs) (matchPart r2 xs)
--matchPart (Repetition r) xs = insert xs (matchPart (Sequence r (Repetition r)) xs)
matchPart (Repetition r) xs = 
  let firstMatch = matchPart r xs
      sequent =  unions $ map (matchPart (Repetition r))
                              (Set.toList (delete xs firstMatch))
  in insert xs (union firstMatch sequent)
r1 = Sequence (Terminal '1') (Repetition (Terminal '0'))
r2 = Repetition (Alternative (Terminal '0') (Terminal '1'))
rhard = Repetition r2




--- TESTS ---
tests = TestList [
   Set.fromList [""] ~=? matchPart (Terminal '1') "1",   
   Set.fromList []   ~=? matchPart (Terminal '0') "1",
   Set.fromList ["1"]   ~=? matchPart (Terminal '0') "01",
   
   Set.fromList [""] ~=? matchPart (Alternative (Terminal '0') (Terminal '1')) "1",
   Set.fromList [""] ~=? matchPart (Alternative (Terminal '0') (Terminal '1')) "0",
   Set.fromList []   ~=? matchPart (Alternative (Terminal '0') (Terminal '1')) "2",
   Set.fromList ["2"]~=? matchPart (Alternative (Terminal '0') (Terminal '1')) "12",
   
   Set.fromList [""]   ~=? matchPart (Sequence (Terminal '0') (Terminal '1')) "01",
   Set.fromList ["0"]  ~=? matchPart (Sequence (Terminal '0') (Terminal '1')) "010",
   Set.fromList []     ~=? matchPart (Sequence (Terminal '0') (Terminal '1')) "10",
   Set.fromList []     ~=? matchPart (Sequence (Terminal '0') (Terminal '1')) "00",
   Set.fromList []     ~=? matchPart (Sequence (Terminal '0') (Terminal '1')) "11",
   
   Set.fromList ["1"]      ~=? matchPart (Repetition (Terminal '0') ) "1",
   Set.fromList [""]       ~=? matchPart (Repetition (Terminal '0') ) "",
   Set.fromList ["", "0"]  ~=? matchPart (Repetition (Terminal '0') ) "0",
   Set.fromList ["1","01","001"]  ~=? matchPart (Repetition (Terminal '0') ) "001",
   Set.fromList ["", "0", "00"]  
      ~=? matchPart (Repetition (Terminal '0') ) "00",
   Set.fromList ["", "0", "00", "000"]  
      ~=? matchPart (Repetition (Terminal '0') ) "000",

   Set.fromList ["", "1"] ~=? matchPart r2 "1",
   Set.fromList ["", "10", "0"] ~=? matchPart r2 "10",
   Set.fromList ["", "10", "010", "0"] ~=? matchPart r2 "010",
   

   Set.fromList ["", "10", "010", "0"] ~=? matchPart r2 "010",


   Set.fromList ["","1", "01", "101"] ~=? matchPart rhard "101"
   ]
                       