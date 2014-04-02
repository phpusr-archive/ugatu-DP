package rsa

import euclide.Euclide
import prime.Prime
import sun.misc.{BASE64Decoder, BASE64Encoder}

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 12:24
 */

/**
 * Алгоритм шифрования RSA (Rivest, Shamir и Adleman)
 */
object RSA {

  /** Максимальное значение простого числа */
  private val PrimeMaxNumber = 100
  /** Максимальное значение открытого ключа */
  private val PublicKeyMaxNumber = 100
  /** Разделитель символов строк */
  private val Splitter = " "
  /** Кодировка строк по умолчанию */
  private val CharsetNameDefault = "utf8"

  /** Логирование */
  private val debugEnable = true
  private def log(s: String) { if (debugEnable) println(s"LOG:: $s") }

  /** Генерация ключей */
  def generateKeys() = {
    val (p, q) = generatePQ(PrimeMaxNumber)
    log(s"p: $p; q: $q")
    val n = p * q
    val publicKey = generatePublicKey(p, q, PublicKeyMaxNumber)
    val privateKey = generatePrivateKey(p, q, publicKey)

    val keys = (n, publicKey, privateKey)
    log(s"keys: $keys")

    keys
  }

  /** Генерация открытого ключа по известным p и q */
  def generatePublicKey(p: Int, q: Int, maxNumber: Int) = {
    val phi = getPhi(p, q)

    var publicKey = 0
    var suitableNumber = false
    do {
      publicKey = Math.round(Math.random()*maxNumber+1).toInt
      suitableNumber = Euclide.gcd(publicKey, phi) == 1
    } while(!suitableNumber)

    publicKey
  }

  /** Генерация закрытого ключа по известным p, q и e */
  def generatePrivateKey(p: Int, q: Int, e: Int) = {
    val phi = getPhi(p, q)

    val gcd = Euclide.gcdExt(e, phi)
    assert(e * gcd.x + phi * gcd.y == gcd.d, "Wrong d")

    if (gcd.x < 0) gcd.x + phi
    else gcd.x
  }

  /** Генерация чисел P и Q */
  def generatePQ(maxNumber: Int) = {
    var (p, q) = (0, 0)
    do {
      p = Prime.generatePrime(maxNumber)
      q = Prime.generatePrime(maxNumber)
    } while(p == q)

    (p, q)
  }

  /** Возвращает число Фи */
  private def getPhi(p: Int, q: Int) = (p-1) * (q-1)

  /** Шифрование строки */
  def encode(message: String, n: Int, publicKey: Int) = {
    val base64String = new BASE64Encoder().encode(message.getBytes(CharsetNameDefault))
    log("base64: " + base64String)
    log("bytes: " + base64String.getBytes(CharsetNameDefault).mkString(" "))

    base64String.getBytes.map { el =>
      modulPow(el, publicKey, n)
    }.mkString(Splitter)
  }

  /** Рашифрование строки */
  def decode(codeMessage: String, n: Int, privateKey: Int) = {
    val split = codeMessage.split(Splitter)
    log(s"split: ${split.mkString(" ")}")
    val bytes = split.map { el =>
      modulPow(el.toInt, privateKey, n).toByte
    }
    log(s"bytes: ${bytes.mkString(" ")}")
    val base64String = new String(bytes, CharsetNameDefault)
    log("base64: " + base64String)

    new String(new BASE64Decoder().decodeBuffer(base64String), CharsetNameDefault)
  }

  /** Возводит число в степень и находит остаток от деления на каждой итерации */
  private def modulPow(value: Int, pow: Int, modulo: Int): Int = {
    var res = value
    for (i <- 2 to pow) {
      res *= value
      res %= modulo
    }

    res
  }

}
