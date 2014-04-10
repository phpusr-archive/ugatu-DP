package dataprotection.lab.three.prime

import org.scalatest.FlatSpec

/**
 *         Date: 02.04.14
 *         Time: 13:21
 */

/**
 * Тестирование класса Prime
 */
class PrimeSpec extends FlatSpec {

  "This numbers dont't" should "be prime" in {
    assert(!Prime.isPrime(100))
    assert(!Prime.isPrime(21))
    assert(!Prime.isPrime(25))
    assert(!Prime.isPrime(105))
  }


  "This number" should "be prime" in {
    assert(Prime.isPrime(2))
    assert(Prime.isPrime(23))
    assert(Prime.isPrime(3))
    assert(Prime.isPrime(103))
    assert(Prime.isPrime(127))
  }

}
