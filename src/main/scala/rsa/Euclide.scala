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
case class GCD(d: Int, x: Int, y: Int)


/**
 * Различные реализации алгоритма Евклида
 */
object Euclide {

  /**
   * Нахождение наибольшего общего делителя
   * Алгоритм Евклида
   * http://www.e-olimp.com/articles/18
   */
  @tailrec
  def gcd(a: Int,b: Int):Int = if (b == 0) a else gcd(b, a % b)

  /**
   * Расширенный алгоритм Евклида
   * Усовершенствованный мной (добавил возвращаемое значение)
   * http://www.e-olimp.com/articles/18
   * Greatest common divisor (НОД)
   */
  def gcdExt(a: Int, b: Int): GCD = {
    if (b == 0) {
      GCD(a, 1, 0)
    } else {
      val res = gcdExt(b, a % b)
      val x = res.y
      val y = res.x - (a/b * res.y)

      GCD(res.d, x, y)
    }
  }

  /**
   * Расширенный алгоритм Евклида из статьи
   * http://landrina.ru/development/c-realizaciya-rasshirennogo-algoritma-evklida/
   */
  def gcdExtV2(aIn: Int, bIn: Int) = {
    var (a, b) = (aIn, bIn)
    var (x, y) = (1, 0)

    while (b > 0) {
      val q = a / b
      val r = a % b

      val tmp = x - q*y
      x = y
      y = tmp

      a = b
      b = r
    }

    y = (a - x * aIn) / bIn

    GCD(a, x, y)
  }

  /**
   * Расширенный алгоритм Евклида
   * http://algolist.manual.ru/maths/teornum/nod.php#4
   */
   def gcdExtV3(aIn: Int, bIn: Int) = {
    var (a, b) = (aIn, bIn)
    var (x, y) = (0, 0)
    var (x1, x2) = (0, 1)
    var (y1, y2) = (1, 0)

    while (b > 0) {
      if (false) println(s"a: $a; b: $b")

      val r = a % b
      val q = a / b

      a = b; b = r

      x = x2 - q*x1; y = y2 - q*y1

      x2 = x1; x1 = x
      y2 = y1; y1 = y
    }

    GCD(a, x2, y2)    
  }

  /** Расширенный алгоритм Евклида из конспкта */
  def gcdExtV4(d: Int, f: Int) = {
    if (gcd(d, f) == 1) 1 //TODO

    val x1 = 1
    val x2 = 0
    var x3 = f

    val y1 = 0
    var y2 = 1
    var y3 = d

    if (y3 == 0) {
      x3 = gcd(d, f) //TODO return
    }

    if (y3 == 1) {
      y3 = gcd(d, f)
      y2 = d % f //TODO мульт. обр. d; return
    }

    var q = x3 / y3
    //TODO 5 шаг
  }

}
