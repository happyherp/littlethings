def sum(f: Int => Int)(a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc
    else loop(a+1, f(a) + acc)
  }
  loop(a, 0)
}

sum(x => x)(1,3)

def squaresSum:(Int,Int) => Int = sum( x => x*x )


def product(f: Int => Int)(a:Int, b:Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc
    else loop(a + 1, f(a) * acc)
  }
  loop(a, 1)
}

product(a => a)(  1,3)

def factorial(a:Int):Int = product(a=>a)(1,a)


factorial(5)



def intervall(initial:Int, combiner: (Int, Int)=> Int)(f:(Int => Int))(a:Int, b:Int):Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc
    else loop(a + 1, combiner(f(a), acc))
  }
  loop(a, initial)
}

def sum2:(Int=>Int)=>(Int, Int) => Int = intervall(0, _+_)

sum2(x => x)(1,3)

val product2:(Int=>Int)=>(Int, Int) => Int = `intervall`(1, _*_)

def factorial2(x:Int):Int = product2(x=>x)(1,x)

factorial2(5)



