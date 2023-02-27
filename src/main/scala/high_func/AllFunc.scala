package high_func

import java.util.Date

object AllFunc {

  def main(args: Array[String]): Unit = {
    /**
     * 递归方法
     * 5的阶乘
     */
    def fun2(num: Int): Int = {
      if (num == 1)
        num
      else
        num * fun2(num - 1)
    }

    println(fun2(5))


    //默认值
//    def fun3(a: Int = 10, b: Int): Unit = {
//      println(a + b)
//    }
//
//    fun3(b = 2)

    /**
     * 可变参数个数的函数
     * 注意：多个参数逗号分开
     */
    def fun4(elements: Int*): Int = {
      var sum: Int = 0;
      for (elem <- elements) {
        sum += elem
      }
      sum
    }

    println(fun4(1, 2, 3, 4))

    /**
     * 匿名函数
     * 1.有参数匿名函数
     * 2.无参数匿名函数
     * 3.有返回值的匿名函数
     * 注意：
     * 可以将匿名函数返回给定义的一个变量
     */
    //有参数匿名函数
    val value1 = (a: Int) => {
      println(a)
    }
    value1(1)
    //无参数匿名函数
    val value2 = () => {
      println("This is test001")
    }
    value2()
    //有返回值的匿名函数
    val value3 = (a: Int, b: Int) => {
      a + b
    }
    println(value3(4, 4))

    /**
     * 嵌套方法
     * 例如：嵌套方法求5的阶乘
     */
    def fun5(num: Int) = {
      def fun6(a: Int, b: Int): Int = {
        if (a == 1) {
          b
        } else {
          fun6(a - 1, a * b)
        }
      }

      fun6(num, 1)
    }

    println(fun5(5))

    /**
     * 偏应用函数
     * 偏应用函数是一种表达式，不需要提供函数需要的所有参数，只需要提供部分，或不提供所需参数
     */
    def log(date: Date, s: String) = {
      println("date is " + date + ",log is " + s)
    }

    val date = new Date()
    log(date, "a")
    log(date, "b")
    log(date, "c")

    //想要调用log，以上变化的是第二个参数，可以用偏应用函数处理
    val logWithDate = log(date, _: String)
    logWithDate("log1")
    logWithDate("log2")
    logWithDate("log3")



    //高阶函数

    //参数即函数

    def function1 (a:Int,b:Int):Int={
      a+b
    }
    def highFuncA(f:(Int,Int)=>Int,c:Int):Int= {
      f(100, c)
    }
    println(highFuncA(function1, 66))

    //函数返回函数
    def highFuncB(a:Int,b:Int):(Int,Int)=>Int={
      def function2(c:Int,d:Int):Int={
        a+b+c+d
      }
      function1
    }

    //函数的参数是函数，函数的返回是函数
    def highFuncC(f:(Int,Int)=>Int):(Int,Int)=>Int={
      f
    }

    val i: Int = highFuncC(function1)(200, 200)
    println(i)
    println(highFuncC((a: Int, b: Int) =>{a+b})(200,200))
    println(highFuncC(_+_)(200,200))

    //柯里化
    def highFuncD(a:Int,b:Int)(c:Int,d:Int):Int={
      a+b+c+d
    }
    println(highFuncD(1,2)(3,4))




  }
}
