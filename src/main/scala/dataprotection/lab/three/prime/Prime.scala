package dataprotection.lab.three.prime

import scala.annotation.tailrec
import dataprotection.lab.three.types.RsaType._
import dataprotection.lab.three.types.RsaType

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 13:27
 */

/**
 * Утилита для генерации простых чисел
 */
object Prime {

  private val debugEnable = false

  /** Генерация простого числа */
  @tailrec
  def generatePrime(maxNumber: RsaNumber): RsaNumber = {
    val number = RsaType.getRandom(maxNumber)
    if (isPrime(number)) number
    else generatePrime(maxNumber)
  }

  /** Проверка, является ли число простым */
  def isPrime(number: RsaNumber) = {
    if (number <= 1) false
    else {
      val res = for (i <- Two to (number/Two) if number % i == 0) yield i
      if (debugEnable) println(number + ": " + res.mkString(", "))

      res.size == 0
    }
  }

}
