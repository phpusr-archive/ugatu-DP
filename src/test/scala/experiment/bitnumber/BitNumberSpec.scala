package experiment.bitnumber

import dataprotection.lab.two.bitnumber.BitNumber
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

  /** Должен создать 32-битный BitNumber из Int */
  it should "create 32 bit number from Int" in {
    val bitNumberOne = BitNumber(Array(4))
    println("4: " + bitNumberOne)

    val expectResultOne = "00000000000000000000000000000100"

    assert(bitNumberOne.toBinStr == expectResultOne)
    assert(bitNumberOne.size == 32)
    assert(bitNumberOne.toString == s"$expectResultOne(32)")

    //----------------------------------

    val bitNumberTwo = BitNumber(Array(-11))
    println("-11: " + bitNumberTwo)

    val expectResultTwo = "11111111111111111111111111110101"

    assert(bitNumberTwo.toBinStr == expectResultTwo)
    assert(bitNumberTwo.size == 32)
    assert(bitNumberTwo.toString == s"$expectResultTwo(32)")
  }

  /** Должен создать 8-битный нулевой BitNumber на основе длины */
  it should "create 8 bit number from size" in {
    val bitNumber = BitNumber(8)
    println(bitNumber)

    assert(bitNumber.toBinStr == "00000000")
  }

  /** Должен получить и установить значение по индексу */
  it should "get & set bit by index" in {
    val bitNumber = BitNumber(Array(4))

    assert(bitNumber(0) == '0')
    assert(bitNumber(1) == '0')
    assert(bitNumber(32-1-2) == '1')

    bitNumber.set(3, true)
    assert(bitNumber(3) == '1')
  }

}
