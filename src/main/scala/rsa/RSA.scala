package rsa

import euclide.Euclide
import prime.Prime

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

  /** Генерация ключей */
  def generateKeys() {
    val (p, q) = generatePQ(PrimeMaxNumber)
    val n = p * q
    val publicKey = generatePublicKey(p, q, PublicKeyMaxNumber)
    val privateKey = generatePrivateKey(p, q, publicKey)

    (n, publicKey, privateKey)
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

    gcd.x
  }

  /** Генерация чисел P и Q */
  def generatePQ(maxNumber: Int) = {
    (Prime.generatePrime(maxNumber), Prime.generatePrime(maxNumber))
  }

  /** Возвращает число Фи */
  private def getPhi(p: Int, q: Int) = (p-1) * (q-1)

}
