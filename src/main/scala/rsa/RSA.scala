package rsa

import scala.annotation.tailrec

/**
 * @author phpusr
 *         Date: 31.03.14
 *         Time: 22:26
 */
object RSA extends App {
  /** Сообщение для шифрования */
  val m = 123

  val p = 47
  val q = 71

  val n = p*q
  val fi = (p-1)*(q-1)

  println(s"p: $p; q: $q; n: $n; fi: $fi\n")

  /** Открытый ключ */
  val e = 79
  /** Закрытый ключ */
  var d = 0

  val a = fi
  val b = e
  println(s"a=$a; b=$b")

  ladrina()

  /**
   * Реализация RSA на основе статьи
   * http://landrina.ru/development/c-sharp-realizaciya-rsa/
   */
  private def ladrina() {
    val dxy = Euclide.extendedEuclide(e % fi, fi)
    println(s"dxy: $dxy")
    d = dxy.d

    testAlgorithm(dxy)
  }

  /** Проверка используемого алгоритма */
  private def testAlgorithm(dxy: DXY) {
    println(s"d: $d")
    println("ax + by: " + (a * dxy.x + b * dxy.y))

    val codeMes = code(m)
    println("\ncode: " + codeMes)
    println("decode: " + decode(codeMes))
  }

  /** Шифрование данных */
  private def code(message: Int) = {
    modulPow(message, e, n)
  }

  /** Рашифрование данных */
  private def decode(codeMessage: Int) = {
    modulPow(codeMessage, d, n)
  }

  /** Возводит число в степень и находит остаток от деления на каждой итерации */
  def modulPow(value: Int, pow: Int, modulo: Int): Int = {
    if (pow == 0) 1
    else (value * modulPow(value, pow-1, modulo)) % modulo
  }

}
