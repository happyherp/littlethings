
data SmallInt = S Integer
    deriving (Show)

likeAnInteger :: (Integer -> Integer) -> SmallInt -> SmallInt
likeAnInteger f (S n) = fromInteger (f n)


instance Num SmallInt where 
    S a + S b = fromInteger $ a+b
    S a * S b = fromInteger $ a*b
    negate = likeAnInteger negate
    abs    = likeAnInteger abs
    signum = likeAnInteger signum
    fromInteger x = S (fromIntegral x `mod ` 10^10)
        
instance Real SmallInt where 
    toRational (S i) = toRational i
    
instance Integral SmallInt where 
  toInteger (S a) = a
  quotRem (S a) (S b) = let 
    (q,r) = quotRem a b
    in  (S $ fromInteger q, S $ fromInteger r)

instance Enum SmallInt where 
    succ = (1+)
    pred x = x - 1
    toEnum i = S $ fromInteger $ fromIntegral i
    fromEnum (S i) = fromIntegral i
   
instance Ord SmallInt where
    compare (S a) (S b) = compare a b
    (S a) <= (S b) = a <= b
    
instance Eq SmallInt where 
    (S a) == (S b) = a == b
    (S a) /= (S b) = a /= b
    
serie :: [SmallInt]
serie = map (\x->x^x) [1..1000]    

result = sum serie