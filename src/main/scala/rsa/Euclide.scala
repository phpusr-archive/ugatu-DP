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
   * http://www.e-olimp.com/articles/18
   */
  def gcdext(a: Int, b: Int, v: DXY) {
    if (b == 0) {
      v.d = a; v.x = 1; v.y = 0
    } else {
      gcdext(b, a % b, v)
      val swap = v.y
      v.y = v.x - (a/b) * v.y
      v.x = swap
    }
  }

  /**
   * Нахождение наибольшего общего делителя
   * http://www.e-olimp.com/articles/18
   */
  @tailrec
  def nod(aInput: Int,bInput: Int): Int = {
    if (bInput == 0) aInput
    else nod(bInput, aInput % bInput)
  }

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
