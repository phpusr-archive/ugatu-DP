package rsa

/**
 * @author phpusr
 *         Date: 02.04.14
 *         Time: 13:21
 */

object TestRSA extends App {

  testGeneratePQ()

  def testGeneratePQ() {
    println(RSA.generatePQ(100))
    println(RSA.generatePQ(100))
    println(RSA.generatePQ(100))
  }

}
