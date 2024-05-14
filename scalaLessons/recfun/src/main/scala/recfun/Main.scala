package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
    def pascal(c: Int, r: Int): Int = c match {
      case 0 | `r` => 1
      case x => pascal(c-1, r-1) + pascal(c, r-1)
    }

  
  /**
   * Exercise 2
   */
    def balance(chars: List[Char]): Boolean = {
      def loop(chars:List[Char], depth:Int):Boolean = chars match {
        case Nil     => depth == 0
        case '('::xs =>               loop(xs, depth+1)
        case ')'::xs => depth >  0 && loop(xs, depth-1)
        case  x ::xs   =>             loop(xs, depth)
      }
      loop(chars, 0)
    }


  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int =  (money, coins) match {
      case (0, _) => 1
      case (_, Nil) => 0
      case (money, coin::rest) =>{
        def loopMultiples(multiplier:Int):Int = {
          val value = multiplier * coin
          if (value <= money)
            countChange(money-value, rest) + loopMultiples(multiplier+1)
          else 0
        }
        loopMultiples(0)
      }
    }
}
