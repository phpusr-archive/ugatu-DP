package dataprotection.lab.three.prime

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 13:21
 */

object TestPrime extends App {

  testIsPrime()

  def testIsPrime() {
    assert(!Prime.isPrime(100))
    assert(!Prime.isPrime(21))
    assert(!Prime.isPrime(25))
    assert(!Prime.isPrime(105))

    assert(Prime.isPrime(23))
    assert(Prime.isPrime(3))
    assert(Prime.isPrime(103))
    assert(Prime.isPrime(127))
  }
}
