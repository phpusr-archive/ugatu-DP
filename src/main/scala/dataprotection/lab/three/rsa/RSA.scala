package dataprotection.lab.three.rsa

import dataprotection.lab.three.euclide.Euclide
import dataprotection.lab.three.prime.Prime
import sun.misc.{BASE64Decoder, BASE64Encoder}
import org.dyndns.phpusr.util.log.Logger
import scala.annotation.tailrec
import dataprotection.lab.three.types.RsaType.RsaNumber
import dataprotection.lab.three.types.{RsaTrait, RsaType}

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 12:24
 */

/**
 * Алгоритм шифрования RSA (Rivest, Shamir и Adleman)
 */
object RSA extends RsaTrait {

  /** Максимальное значение простого числа */
  val PrimeMaxNumber = 100
  /** Максимальное значение открытого ключа */
  val PublicKeyMaxNumber = 100
  /** Разделитель символов строк */
  private val Splitter = " "
  /** Кодировка строк по умолчанию */
  private val CharsetNameDefault = "utf8"

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Генерация ключей */
  def generateKeys() = {
    logger.title("generateKeys")
    val (p, q) = generatePQ(PrimeMaxNumber)
    logger.debug(s"p: $p; q: $q")
    val n = p * q
    val publicKey = generatePublicKey(p, q, PublicKeyMaxNumber)
    val privateKey = generatePrivateKey(p, q, publicKey)

    val keys = (p, q, n, publicKey, privateKey)
    logger.debug(s"keys: $keys")

    keys
  }

  /** Генерация открытого ключа по известным p и q */
  @tailrec
  def generatePublicKey(p: RsaNumber, q: RsaNumber, maxNumber: RsaNumber): RsaNumber = {
    val phi = getPhi(p, q)
    val startValue = 2
    val publicKey = RsaType.getRandom(maxNumber-startValue) + startValue

    // Проверка, удовлетворяет ли число равенству
    //  Открытый ключ не должен иметь общих делителей (кроме 1) с Фи
    if (Euclide.gcd(publicKey, phi) == RsaType.One) publicKey
    else generatePublicKey(p, q, maxNumber)
  }

  /** Генерация закрытого ключа по известным p, q и e */
  def generatePrivateKey(p: RsaNumber, q: RsaNumber, e: RsaNumber) = {
    val phi = getPhi(p, q)

    val gcd = Euclide.gcdExt(e, phi)
    assert(e * gcd.x + phi * gcd.y == gcd.d, "Wrong d")

    if (gcd.x < 0) gcd.x + phi
    else gcd.x
  }

  /** Генерация чисел P и Q */
  @tailrec
  def generatePQ(maxNumber: RsaNumber) : (RsaNumber, RsaNumber) = {
    // p и q должны быть больше этого числа, иначе могут быть косяки
    val FixNumber = 13
    val p = Prime.generatePrime(maxNumber)
    val q = Prime.generatePrime(maxNumber)

    if (p != q && p >= FixNumber && q >= FixNumber) (p, q)
    else generatePQ(maxNumber)
  }

  /** Возвращает число Фи */
  private def getPhi(p: RsaNumber, q: RsaNumber) = (p-1) * (q-1)

  /** Шифрование числа TODO test, rename */
  def encodeNumber(message: String, n: RsaNumber, publicKey: RsaNumber) = {
    logger.title(s"encodeNumber($message)")
    
    // Размер блока числа
    val blockSize = n.toString.size - 1

    // Разбиваем строку, на подстроки по blockSize-символов
    val reverseblocks = message.reverse.split(s"(?<=\\G.{$blockSize})")
    val blocks = reverseblocks.reverse.map {el => el.reverse}

    // Шифрует каждый блок цифр, результаты склеивает
    val encodeMessage = blocks.map { block =>
      logger.trace(s"block: $block")
      modulPow(block.toRsaNumber, publicKey, n)
    }.mkString(Splitter)
    logger.debug(s"encodeMessage: $encodeMessage")

    encodeMessage
  }

  /** Расшифрование числа */
  def decodeNumber(encodeMessage: String, n: RsaNumber, privateKey: RsaNumber) = {
    logger.title(s"decodeNumber($encodeMessage)")

    // Размер блока числа
    val blockSize = n.toString.size - 1

    // Разбиение шифрованной строки на части и расшифровка частей, с последующей склейкой
    val decodeMessageWithZeros = encodeMessage.split(Splitter).map { el =>
      logger.trace(s"el: $el")
      val decodeBlock = modulPow(el.toRsaNumber, privateKey, n)
      val format = s"%0${blockSize}d"
      decodeBlock formatted format
    }.mkString
    logger.trace(s"decodeMessageWithZeros: $decodeMessageWithZeros")

    // Избавление от нулей в начале
    val decodeMessage = removeFirstZeros(decodeMessageWithZeros)
    logger.debug(s"decodeMessage: $decodeMessage")

    decodeMessage
  }

  /** Избавление от нулей в начале */
  @tailrec
  private def removeFirstZeros(str: String): String = {
    if (str.size == 0 || str.head != '0') str
    else removeFirstZeros(str.tail)
  }

  /** Шифрование строки */
  def encodeString(message: String, n: RsaNumber, publicKey: RsaNumber) = {
    logger.title(s"encodeString($message)")

    val base64String = new BASE64Encoder().encode(message.getBytes(CharsetNameDefault))
    logger.trace("base64: " + base64String)
    logger.trace("bytes: " + base64String.getBytes(CharsetNameDefault).mkString(" "))

    val encodeMessage = base64String.getBytes(CharsetNameDefault).map { el =>
      modulPow(el, publicKey, n)
    }.mkString(Splitter)
    logger.debug(s"encodeMessage: $encodeMessage")

    encodeMessage
  }

  /** Рашифрование строки */
  def decodeString(encodeMessage: String, n: RsaNumber, privateKey: RsaNumber) = {
    logger.title(s"decodeString($encodeMessage)")

    val split = encodeMessage.split(Splitter)
    logger.trace(s"split: ${split.mkString(" ")}")
    val bytes = split.map { el =>
      modulPow(el.toRsaNumber, privateKey, n).toByte
    }
    logger.trace(s"bytes: ${bytes.mkString(" ")}")
    val base64String = new String(bytes, CharsetNameDefault)
    logger.trace("base64: " + base64String)

    val decodeMessage = new String(new BASE64Decoder().decodeBuffer(base64String), CharsetNameDefault)
    logger.debug(s"decodeMessage: $decodeMessage")

    decodeMessage
  }

  /** Возводит число в степень и находит остаток от деления на каждой итерации */
  private def modulPow(value: RsaNumber, pow: RsaNumber, modulo: RsaNumber): RsaNumber = {
    var res = value
    for (i <- RsaType.Two to pow) {
      res *= value
      res %= modulo
    }

    res
  }

}
