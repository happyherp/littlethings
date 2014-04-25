module Re where

data RegExp a = Terminal a 
               | Sequence (RegExp a) (RegExp a)
               | Alternative (RegExp a) (RegExp a)
               | Repetition (RegExp a) 
   deriving Show