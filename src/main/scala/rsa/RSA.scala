package rsa

import euclide.Euclide
import prime.Prime
import sun.misc.{BASE64Decoder, BASE64Encoder}
import org.dyndns.phpusr.util.log.Logger

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
  private val l = Logger()

  /** Генерация ключей */
  def generateKeys() = {
    l.title("generateKeys")
    val (p, q) = generatePQ(PrimeMaxNumber)
    l.debug(s"p: $p; q: $q")
    val n = p * q
    val publicKey = generatePublicKey(p, q, PublicKeyMaxNumber)
    val privateKey = generatePrivateKey(p, q, publicKey)

    val keys = (n, publicKey, privateKey)
    l.debug(s"keys: $keys")

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

  /** Шифрование числа TODO test, rename */
  def encodeNumber(message: String, n: Int, publicKey: Int) = {
    l.title(s"encodeNumber($message)")
    
    // Размер блока числа
    val blockSize = n.toString.size - 1

    // Разбиваем строку, на подстроки по blockSize-символов
    val blocks = message.split(s"(?<=\\G.{$blockSize})")

    // Шифрует каждый блок цифр, результаты склеивает
    val encodeMessage = blocks.map { block =>
      l.trace(s"block: $block")
      modulPow(block.toInt, publicKey, n)
    }.mkString(Splitter)
    l.debug(s"encodeMessage: $encodeMessage")

    encodeMessage
  }

  /** Расшифрование числа */
  def decodeNumber(encodeMessage: String, n: Int, privateKey: Int) = {
    l.title(s"decodeNumber($encodeMessage)")

    // Размер блока числа
    val blockSize = n.toString.size - 1

    // Разбиение шифрованной строки на части и расшифровка частей, с последующей склейкой
    val decodeMessage = encodeMessage.split(Splitter).map { el =>
      l.trace(s"el: $el")
      val decodeBlock = modulPow(el.toInt, privateKey, n)
      val format = s"%0${blockSize}d"
      decodeBlock formatted format
    }.mkString
    l.debug(s"decodeMessage: $decodeMessage")

    decodeMessage
  }

  /** Шифрование строки */
  def encodeString(message: String, n: Int, publicKey: Int) = {
    l.title(s"encodeString($message)")

    val base64String = new BASE64Encoder().encode(message.getBytes(CharsetNameDefault))
    l.trace("base64: " + base64String)
    l.trace("bytes: " + base64String.getBytes(CharsetNameDefault).mkString(" "))

    val encodeMessage = base64String.getBytes(CharsetNameDefault).map { el =>
      modulPow(el, publicKey, n)
    }.mkString(Splitter)
    l.debug(s"encodeMessage: $encodeMessage")

    encodeMessage
  }

  /** Рашифрование строки */
  def decodeString(encodeMessage: String, n: Int, privateKey: Int) = {
    l.title(s"decodeString($encodeMessage)")

    val split = encodeMessage.split(Splitter)
    l.trace(s"split: ${split.mkString(" ")}")
    val bytes = split.map { el =>
      modulPow(el.toInt, privateKey, n).toByte
    }
    l.trace(s"bytes: ${bytes.mkString(" ")}")
    val base64String = new String(bytes, CharsetNameDefault)
    l.trace("base64: " + base64String)

    val decodeMessage = new String(new BASE64Decoder().decodeBuffer(base64String), CharsetNameDefault)
    l.debug(s"decodeMessage: $decodeMessage")

    decodeMessage
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
