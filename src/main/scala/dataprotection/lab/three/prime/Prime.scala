package dataprotection.lab.three.prime

import scala.util.Random
import scala.annotation.tailrec

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
  def generatePrime(maxNumber: Int): Int = {
    val number = Random.nextInt(maxNumber)
    if (isPrime(number)) number
    else generatePrime(maxNumber)
  }

  /** Проверка, является ли число простым */
  def isPrime(number: Int) = {
    if (number <= 1) false
    else {
      val res = for (i <- 2 to number/2 if number % i == 0) yield i
      if (debugEnable) println(number + ": " + res.mkString(", "))

      res.size == 0
    }
  }

}
