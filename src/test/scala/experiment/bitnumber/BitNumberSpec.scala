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

  /** Должен создать 32-битный BitNumber из Array[Int] */
  it should "create 32 bit number from Array[Int]" in {
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

  /** Должен создать 8-битный BitNumber из Array[Byte] */
  it should "create 8 bit number from Array[Byte]" in {
    var bitNumber = BitNumber(Array(10.toByte))
    println("10: " + bitNumber)

    var expectResult = "00001010"

    assert(bitNumber.toBinStr == expectResult)
    assert(bitNumber.size == 8)
    assert(bitNumber.toString == s"$expectResult(8)")

    //----------------------------------

    bitNumber = BitNumber(Array(-10.toByte))
    println("-10: " + bitNumber)

    expectResult = "11110110"

    assert(bitNumber.toBinStr == expectResult)
    assert(bitNumber.size == 8)
    assert(bitNumber.toString == s"$expectResult(8)")
  }

  /** Должен создать 8-битный нулевой BitNumber на основе длины */
  it should "create 8 bit number from size" in {
    val bitNumber = BitNumber(8)
    println("0: " + bitNumber)

    assert(bitNumber.toBinStr == "00000000")
  }

  /** Должен получить и установить значение по индексу */
  it should "get & set bit by index" in {
    val bitNumber = BitNumber(Array(4))

    assert(bitNumber(0) == '0')
    assert(bitNumber(1) == '0')
    assert(bitNumber(32-1-2) == '1')

    bitNumber.set(3, BitNum.One)
    assert(bitNumber(3) == '1')
  }

  /** Должен соединить 2 BitNumber */
  it should "join 2 BitNumber" in {
    val array = Array(-10.toByte, 2.toByte)
    val bitNumber = BitNumber(array)
    println(s"${array.mkString("|")}: $bitNumber")

    val expectResult = "1111011000000010"

    assert(bitNumber.toBinStr == expectResult)
    assert(bitNumber.size == 16)
    assert(bitNumber.toString == s"$expectResult(16)")
  }

}
