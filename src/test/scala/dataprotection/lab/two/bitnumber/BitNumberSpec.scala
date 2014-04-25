package dataprotection.lab.two.bitnumber

import org.scalatest.FlatSpec

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 11:38
 */

/**
 * Тест для BitNumber
 */
class BitNumberSpec extends FlatSpec {

  /** Тестирование создания BitNumber из Int */
  it should "create 32 bit number from Int" in {
    val bitNumberOne = BitNumber(4)
    println("4: " + bitNumberOne)

    val expectResultOne = "00000000000000000000000000000100"

    assert(bitNumberOne.toBinStr == expectResultOne)
    assert(bitNumberOne.size == 32)
    assert(bitNumberOne.toString == s"$expectResultOne(32)")

    //----------------------------------

    val bitNumberTwo = BitNumber(-11)
    println("-11: " + bitNumberTwo)

    val expectResultTwo = "11111111111111111111111111110101"

    assert(bitNumberTwo.toBinStr == expectResultTwo)
    assert(bitNumberTwo.size == 32)
    assert(bitNumberTwo.toString == s"$expectResultTwo(32)")
  }

}
