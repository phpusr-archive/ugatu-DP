package experiment.rsa

import sun.misc.{BASE64Decoder, BASE64Encoder}
import dataprotection.lab.three.euclide.{Euclide, GCD}

/**
 * @author phpusr
 *         Date: 31.03.14
 *         Time: 22:26
 */

/**
 * TODO Нужно сделать отдельное шифрование для чисел, деля их на разряды
 */
object RSA_App extends App {
  /** Сообщение для шифрования */
  val m = "mr. President"
  val splitter = " "

  val p = 47
  val q = 71

  val n = p*q
  val fi = (p-1)*(q-1)

  println(s"p: $p; q: $q; n: $n; fi: $fi\n")

  /** Открытый ключ */
  val e = 79
  /** Закрытый ключ */
  var d = 0

  //ladrina()
  eolimp()

  /**
   * Моя реализация, используя алгоритм из статьи
   * http://www.e-olimp.com/articles/18
   */
  private def eolimp() {
    println("\neolimp()")

    val dxy = Euclide.gcdExt(e, fi)
    println(s"dxy: $dxy")
    d = dxy.x // x - оказывается надо было брать, а не d

    testAlgorithm(e, fi, dxy)
  }

  /**
   * Реализация RSA на основе статьи
   * <a href="http://landrina.ru/development/c-sharp-realizaciya-rsa/">link</a>
   */
  private def ladrina() {
    println("\nladrina()")

    val dxy = Euclide.gcdExtV2(e % fi, fi)
    println(s"dxy: $dxy")
    d = dxy.d

    testAlgorithm(e, fi, dxy)
  }

  /** Проверка используемого алгоритма */
  private def testAlgorithm(a: Int, b: Int, dxy: GCD) {
    println(s"d: $d")
    println("ax + by: " + (a * dxy.x + b * dxy.y))

    val codeMes = encode(m)
    println("\ncode: " + codeMes.mkString(" "))
    println("decode: " + decode(codeMes))
  }

  /** Шифрование данных */
  private def encode(message: String) = {
    val base64String = new BASE64Encoder().encode(message.getBytes)
    println("base64: " + base64String)

    base64String.getBytes.map { el =>
      modulPow(el, e, n)
    }.mkString(splitter)
  }

  /** Рашифрование данных */
  private def decode(codeMessage: String) = {
    val base64String = new String(codeMessage.split(splitter).map { el =>
      modulPow(el.toInt, d, n).toByte
    })
    println("base64: " + base64String)

    new String(new BASE64Decoder().decodeBuffer(base64String))
  }

  /** Возводит число в степень и находит остаток от деления на каждой итерации */
  def modulPow(value: Int, pow: Int, modulo: Int): Int = {
    if (pow == 0) 1
    else (value * modulPow(value, pow-1, modulo)) % modulo
  }

}
