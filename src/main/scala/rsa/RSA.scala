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

  ladrina()
  eolimp()

  /**
   * Моя реализация, используя алгоритм из статьи
   * http://www.e-olimp.com/articles/18
   */
  private def eolimp() {
    println("\neolimp()")

    val dxy = DXY(0,0,0)
    Euclide.gcdext(e, fi, dxy)
    println(s"dxy: $dxy")
    d = dxy.x // x - оказывается надо было брать, а не d

    testAlgorithm(e, fi, dxy)
  }

  /**
   * Реализация RSA на основе статьи
   * http://landrina.ru/development/c-sharp-realizaciya-rsa/
   */
  private def ladrina() {
    println("\nladrina()")

    val dxy = Euclide.extendedEuclide(e % fi, fi)
    println(s"dxy: $dxy")
    d = dxy.d

    testAlgorithm(e, fi, dxy)
  }

  /** Проверка используемого алгоритма */
  private def testAlgorithm(a: Int, b: Int, dxy: DXY) {
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
