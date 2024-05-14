import Prelude hiding ((^))

data X = Tree [X]
x = Tree [x,x]

data U = U { f :: U -> Bool }

phi = U $ const False
x `e` y = f y x



neg a = U $ \x -> not (x `e` a)
a \/ b = U $ \x -> x `e` a || x `e` b
a /\ b = U $ \x -> x `e` a && x `e` b

--set of all sets that don't contain themselves
nonSelfContaining = U $ \a -> not (a `e` a)