package rsa

/**
 * @author phpusr
 *         Date: 01.04.14
 *         Time: 14:53
 */

/**
 * Тестирование алгоритмов Евклида
 */
object TestEuclide extends App {
  val a = 79
  val b = 3220

  testExtendedEuclide()
  testGcdExt()
  testExtEvklid()

  def testExtendedEuclide() {
    println("\ntestExtendedEuclide()")

    val dxy = Euclide.extendedEuclide(a, b)
    println(s"dxy: $dxy")
  }

  def testGcdExt() {
    println("\ntest_gcdext()")

    val dxy = Euclide.gcdExt(a, b)
    println(s"dxy: $dxy")
  }

  def testExtEvklid() {
    println("\ntestExtEvklid()")

    val dxy = Euclide.extEvklid(a, b)
    println(s"dxy: $dxy")
  }

}
