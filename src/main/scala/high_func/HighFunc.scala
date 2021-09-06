package high_func

object HighFunc {
  def main(args: Array[String]): Unit = {
    def add(x:Int,y:Long):Double=x.toDouble+y
    val addSpicy=CurryImplement.curry(add)
    println(addSpicy(3)(1L)) //4.0

    val increment=addSpicy(2)
    println(increment(1L)) //3.0
    val unSpicedAdd=CurryImplement.uncurry(addSpicy)
    println(unSpicedAdd(1,6L)) //7.0

  }
}

trait Curry{
  def curry[A,B,C](f:(A,B)=>C):A=>B=>C
  def uncurry[A,B,C](f:A=>B=>C):(A,B)=>C
}

object CurryImplement extends Curry{
  override def curry[A, B, C](f: (A, B) => C): A => B => C = {(a:A)=>(b:B)=>f(a,b)}

  override def uncurry[A, B, C](f: A => B => C): (A, B) => C = {(a:A,b:B)=>f(a)(b)}
}