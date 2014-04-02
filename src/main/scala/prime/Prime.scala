package prime

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
  def generatePrime(maxNumber: Int) = {
    var number = 0
    do {
      number = Math.round(Math.random()*maxNumber+1).toInt
    } while(!isPrime(number))

    number
  }

  /** Проверка, является ли число простым */
  def isPrime(number: Int) = {
    if (number == 1) false
    else {
      val res = for (i <- 2 to number/2 if number % i == 0) yield i
      if (debugEnable) println(number + ": " + res.mkString(", "))

      res.size == 0
    }
  }

}
