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
    // Числа <= 1 не простые
    if (number <= One) false
    // Поиск простых чисел, находя их делителей от 2 до половины самого числа
    else {
      // По умолчанию число простое
      //  как только будет найден делитель от 2 до number/2
      //  число не простое
      var prime = true
      var divider = Two
      val endNumber = number / Two
      while(prime && divider <= endNumber) {
        prime = number % divider != 0
        if (debugEnable) println(s"num: $number; diveder: $divider - $prime")
        divider += One
      }

      prime
    }
  }

}
