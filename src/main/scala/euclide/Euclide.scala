package euclide

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

      a = b;  b = r

      val tmp = x - q*y
      x = y
      y = tmp
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
      val q = a / b
      val r = a % b

      a = b; b = r

      x = x2 - q*x1; y = y2 - q*y1

      x2 = x1; x1 = x
      y2 = y1; y1 = y
    }

    GCD(a, x2, y2)    
  }

  /**
   * Расширенный алгоритм Евклида из конспкта
   * !!! Не работает (непонятен его смысл)
   */
  private def gcdExtV4(d: Int, f: Int): Int = {
    // Шаг 1
    var (x1, x2, x3) = (1, 0, f)
    var (y1, y2 ,y3) = (0, 1, d)

    // Шаг 2
    if (y3 == 0) {
      x3 = gcdExtV4(d, f) //TODO return (нет обратного)
    }

    // Шаг 2
    if (y3 == 1) {
      y3 = gcdExtV4(d, f)
      y2 = d % f //TODO мульт. обр. d; return
    }

    // Шаг 4
    val q = x3 / y3

    // Шаг 5
    val (t1, t2, t3) = (x1-q*y1, x2-q*y2, x3-q*y3)

    // Шаг 6
    (x1, x2, x3) = (y1, y2, y3)

    // Шаг 7
    (y1, y2, y3) = (t1, t2, t3)

    // Шаг 8
    // TODO goto 2

    0
  }

}
