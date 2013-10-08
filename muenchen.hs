data Term = Con Int | Div Term Term

eval :: Term -> M Int
eval (Con i) = Return i
eval (Div t1 t2) = 
  case (eval t1) of 
     Return n1 -> case (eval t2) of
        Return 0 -> Raise "Zero div"
        Return n2 -> Return $ n1 `div` n2
        x -> x
     x -> x


countDiv :: Term -> Int
countDiv (Con x) = 0
countDiv (Div t1 t2) = countDiv t1 + (countDiv t2) +1


t = (Div (Con 1) (Div (Con  4) (Con 1)))

data M a = Raise Exception | Return a
  deriving Show
type Exception = String



lala :: Monad  m => m a -> (a -> b ) -> m b
lala a f = a >>= (return . f)

writeNTimes :: Int -> String -> IO ()
writeNTimes 0 s = return ()
writeNTimes n s = putStr s >> (writeNTimes (n-1) s)
