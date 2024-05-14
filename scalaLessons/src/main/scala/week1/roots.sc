import Math.abs

object session {

  def sqRoot(x:Double):Double
  = {

    def sqRootIter(guess: Double): Double
    = if (isGoodEnough(guess)) guess
    else sqRootIter(improve(guess))

    def isGoodEnough(guess: Double): Boolean
    = abs(guess * guess - x)/x < 0.01

    def improve(guess: Double): Double
    =  (x / guess + guess )/2

    sqRootIter(1)
  }


  sqRoot(2)

  sqRoot(4)

  sqRoot(9)

  sqRoot(10)

  sqRoot(16)

  sqRoot(0.25)

  sqRoot(0.000025)

  sqRoot(1.0e50)
}




