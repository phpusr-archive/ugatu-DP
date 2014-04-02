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

  /** Генерация ключей */
  def generateKeys() {
    val (p, q) = generatePQ(PrimeMaxNumber)
    val n = p * q
    val phi = getPhi(p, q) //TODO
    val publicKey = generatePublicKey()
    val privateKey = generatePrivateKey(p, q, publicKey)

    (n, publicKey, privateKey)
  }

  def generatePublicKey() = {
    10 //TODO
  }

  /** Генерация открытого ключа по известным p и q */
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
