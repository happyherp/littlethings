
{-# LANGUAGE MultiParamTypeClasses #-}
{-# LANGUAGE TypeSynonymInstances  #-}
 
-- The following compiler options are neccessary
-- for the extension concerning the error handling
-- they are not required for this simple example
 
-- ----------------------------------------
--
-- Monadic version of simple expression
-- evaluator.
-- No extensions, same semantics as in Expr0
--
-- ----------------------------------------
 
module Expr1 where
 
import           Control.Monad ()
 
-- ----------------------------------------
-- syntactic domains
 
data Expr  = Const  Int
           | Binary BinOp Expr Expr
             deriving (Show)
 
data BinOp = Add | Sub | Mul | Div | Mod
             deriving (Eq, Show)
 
-- ----------------------------------------
-- semantic domains
 
data Result a
           = Val { val ::a } | Error String
             deriving (Show)
 
-- ----------------------------------------
-- the identity monad
 
instance Monad Result where
  return        = Val
  (Val v) >>= g = g v
  (Error e) >>= g = Error e
 
-- ----------------------------------------
-- the meaning of an expression
 
eval :: Expr -> Result Int
eval (Const i)
  = return i
 
eval (Binary op l r)
  = do
    mf <- lookupMft op
    mf (eval l) (eval r)
 
-- ----------------------------------------
-- the meaning of binary operators
 
type MF = Result Int -> Result Int ->
          Result Int
 
lookupMft :: BinOp -> Result MF
lookupMft op
  = case lookup op mft of
    Nothing -> Error
               "operation not implemented"
    Just mf -> return mf
 
mft :: [(BinOp, MF)]
mft
  = [ (Add, liftM2 (+))
    , (Sub, liftM2 (-))
    , (Mul, liftM2 (*))
    , (Div, customdiv)
    ]


customdiv :: MF
customdiv _ (Val 0) = Error "Division by zero.hihi"
customdiv v1 v2 = liftM2 div v1 v2

-- defined in Control.Monad
 
liftM2  :: (Monad m) => (a1 -> a2 -> r) -> m a1 -> m a2 -> m r
liftM2 f m1 m2
    = do
      x1 <- m1
      x2 <- m2
      return (f x1 x2)
 
-- ----------------------------------------
-- sample expressions
 
e1 = Binary Mul (Binary Add (Const 2)
                            (Const 4)
                )
                (Const 7)
e2 = Binary Div (Const 1) (Const 0)
e3 = Binary Mod (Const 1) (Const 0)
 
v1 = eval e1
 
-- ----------------------------------------

