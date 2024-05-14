def fak(a:Double):Double = if (a == 1)  1 else fak(a-1) * a

fak(6)

def loop:Double = loop+1

def fst(a:Any,b: =>Any) = a

fst(1,loop)


if (1 > 2)"yeah" else "Boo"

val f15 = fak(15)


def and(x:Boolean, y: => Boolean):Boolean = if (x) y else false
def or(x:Boolean, y: => Boolean):Boolean = if (x) true else y

and(false, fak(10000) > 10)



sqrt x = fixedPoint(averageDamp(x=>x/y))(1)