package dataprotection.lab.three.euclide

import org.scalatest.FlatSpec

/**
 * @author phpusr
 *         Date: 01.04.14
 *         Time: 14:53
 */

/**
 * Тестирование алгоритмов Евклида
 */
class EuclideSpec extends FlatSpec {
  val a = 79
  val b = 3220
  val res = GCD(1,1019,-25)

  "External Euclide" should s"return $res" in {
    val gcd = Euclide.gcdExt(a, b)
    info(gcd.toString)

    assert(res == gcd)
  }

  "External Euclide v2" should s"return $res" in {
    val gcd = Euclide.gcdExtV2(a, b)
    info(gcd.toString)

    assert(res == gcd)
  }

  "External Euclide v3" should s"return $res" in {
    val gcd = Euclide.gcdExtV3(a, b)
    info(gcd.toString)

    assert(res == gcd)
  }

}
