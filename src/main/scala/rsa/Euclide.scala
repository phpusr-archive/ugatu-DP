package rsa

import scala.annotation.tailrec

/**
 * @author phpusr
 *         Date: 01.04.14
 *         Time: 14:35
 */

/**
 * Выходндые значения расширенного алгоритма Евклида
 */
case class DXY(var d: Int, var x: Int, var y: Int)


/**
 * Различные реализации алгоритма Евклида
 */
object Euclide {

  /**
   * Расширенный алгоритм Евклида из статьи
   * http://landrina.ru/development/c-realizaciya-rasshirennogo-algoritma-evklida/
   */
  def extendedEuclide(aIn: Int, bIn: Int) = {
    var x = 1
    var a = aIn
    var y = 0
    var b = bIn

    while (b > 0) {
      val q = a / b
      val r = a % b

      val tmp = x - q*y
      x = y
      y = tmp

      a = b
      b = r
    }

    val tmp = a - x * aIn
    val res = tmp / bIn

    DXY(x, res, a)
  }

  /**
   * Расширенный алгоритм Евклида
   * http://algolist.manual.ru/maths/teornum/nod.php#4
   */
   def extEvklid(aIn: Int, bIn: Int) = {
    var a = aIn
    var b = bIn

    var x = 0
    var y = 0

    /*if (a > b) {
      println("a > b")
    } else*/ if (b == 0) {
      DXY(a, 1, 0)
    } else {
      var x1 = 0; var x2 = 1
      var y1 = 1; var y2 = 0

      while (b > 0) {
        if (false) println(s"a: $a; b: $b")

        val r = a % b
        val q = a / b

        a = b; b = r

        x = x2 - q*x1; y = y2 - q*y1

        x2 = x1; x1 = x
        y2 = y1; y1 = y
      }

      DXY(a, x2, y2)
    }
  }


  /**
   * Расширенный алгоритм Евклида
   * http://www.e-olimp.com/articles/18
   */
  def gcdext(a: Int, b: Int, v: DXY) {
    if (b == 0) {
      v.d = a; v.x = 1; v.y = 0
    } else {
      gcdext(b, a % b, v)
      val yCopy = v.y
      v.y = v.x - (a/b * v.y)
      v.x = yCopy
    }
  }

  /**
   * Нахождение наибольшего общего делителя
   * http://www.e-olimp.com/articles/18
   */
  @tailrec
  def nod(a: Int,b: Int):Int = if (b == 0) a else nod(b, a % b)

  /** Расширенный алгоритм Евклида из конспкта */
  def evklid(d: Int, f: Int) = {
    if (nod(d, f) == 1) 1 //TODO

    val x1 = 1
    val x2 = 0
    var x3 = f

    val y1 = 0
    var y2 = 1
    var y3 = d

    if (y3 == 0) {
      x3 = nod(d, f) //TODO return
    }

    if (y3 == 1) {
      y3 = nod(d, f)
      y2 = d % f //TODO мульт. обр. d; return
    }

    var q = x3 / y3
    //TODO 5 шаг
  }

}
