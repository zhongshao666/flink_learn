package implicit_

import ComplexImplicits._
import scala.language.implicitConversions

class Complex(val real: Double, val imaginary: Double) {
  def plus(that: Complex): Complex = new Complex(this.real + that.real, this.imaginary + that.imaginary)
  def plus(n: Double): Complex = new Complex(this.real + n, this.imaginary)
  def minus(that: Complex): Complex = new Complex(this.real - that.real, this.imaginary - that.imaginary)
  def unary(): Double = {
    val value: Double = Math.sqrt(real * real + imaginary * imaginary)
    value
  }
  override def toString: String = real + " + " + imaginary + "i"
}

object ComplexImplicits {
  implicit def Double2Complex(value: Double): Complex = new Complex(value, 0.0)
  implicit def Tuple2Complex(value: Tuple2[Double, Double]): Complex = new Complex(value._1, value._2)

}

object UsingImplicitConversion {
  def main(args: Array[String]): Unit = {
    val obj: Complex = new Complex(5.0, 6.0)
    val x: Complex = new Complex(4.0, 3.0)
    val y: Complex = new Complex(8.0, -7.0)
    println(x) // prints 4.0 + 3.0i
    println(x plus y) // prints 12.0 + -4.0i
    println(x minus y) // -4.0 + 10.0i
    println(obj.unary()) // prints 7.810249675906654

    val z: Complex = 4 plus y    //隐式调用 Double2Complex     去掉import ComplexImplicits._可以看出
    println(z) // prints 12.0 + -7.0i
    val p: Complex = (1.0, 1.0) plus z  //隐式调用 Tuple2Complex
    println(p) // prints 13.0 + -6.0i

  }
}
