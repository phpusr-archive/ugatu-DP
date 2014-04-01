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
  test_gcdext()

  def testExtendedEuclide() {
    println("\ntestExtendedEuclide()")

    val dxy = Euclide.extendedEuclide(a, b)
    println(s"dxy: $dxy")
  }

  def test_gcdext() {
    println("\ntest_gcdext()")

    val dxy = new DXY(0,0,0)
    Euclide.gcdext(a % b, b, dxy)
    println(s"dxy: $dxy")
  }


}
