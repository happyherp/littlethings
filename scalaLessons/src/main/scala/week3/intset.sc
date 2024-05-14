abstract class IntSet {
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(other: IntSet): IntSet
}

object Empty extends IntSet{
  def incl(x:Int): IntSet = new NonEmpty(x, Empty, Empty)

  override def contains(x: Int): Boolean = false

  override def union(other: IntSet): IntSet = other
}


class NonEmpty(elem:Int, left: IntSet, right:IntSet) extends IntSet{

  override def incl(x: Int): IntSet =
    if (x < elem) new NonEmpty(elem,left.incl(x), right)
    else if (x > elem) new NonEmpty(elem, left, right.incl(x))
    else this

  override def contains(x: Int): Boolean =
    if (x<elem) left.contains(x)
    else if (x>elem) right.contains(x)
    else true

  override def union(other: IntSet): IntSet =
    other.incl(elem).union(left).union(right)

}