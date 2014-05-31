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

  /** Сдвиг влево */
  it should "left shift" in {
    var a = BitNumber(Array(10.toByte))

    var res = a < 3
    var expect = "00001010000"

    assert(res.toBinStr == expect)
    assert(res.size == 11)

    //--------------------------------
    a = BitNumber(Array(5.toByte))

    res = a < 2
    expect = "0000010100"

    assert(res.toBinStr == expect)
    assert(res.size == 10)
  }

  /** Клонирование */
  it should "clone" in {
    val a = BitNumber(Array(10.toByte))
    val clone = a.clone

    assert(a != clone)
    assert(a.toBinStr == clone.toBinStr)
    clone.set(5, BitNum.One)
    assert(a.toBinStr != clone.toBinStr)
  }

  /** Побитовое умножение */
  it should "bit multiply" in {
    var a = BitNumber(Array(10.toByte))
    var b = BitNumber(Array(8.toByte))

    var res = a * b
    var expect = "00001000"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

    //--------------------------------

    a = BitNumber(Array(4.toByte))
    b = BitNumber(Array(5.toByte))

    res = a * b
    expect = "00000100"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

    //--------------------------------

    a = BitNumber(Array(10.toByte))
    b = BitNumber(Array(8))

    intercept [IllegalArgumentException] {
      a * b
    }
  }

  /** Умножение на бит */
  it should "multiply on bit" in {
    var a = BitNumber(Array(10.toByte))
    var b = BitNum.One

    var res = a * b
    var expect = "00001010"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

    //--------------------------------
    a = BitNumber(Array(4.toByte))
    b = BitNum.Zero

    res = a * b
    expect = "00000000"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

  }

  /** XOR (Сложение по модулю 2) */
  it should "xor" in {
    var a = BitNumber(Array(10.toByte))
    var b = BitNumber(Array(8.toByte))

    var res = a xor b
    var expect = "00000010"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

    //--------------------------------

    a = BitNumber(Array(4.toByte))
    b = BitNumber(Array(5.toByte))

    res = a xor b
    expect = "00000001"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

    //--------------------------------

    a = BitNumber(Array(10.toByte))
    b = BitNumber(Array(8))

    intercept [IllegalArgumentException] {
      a * b
    }
  }

  /** Сложение */
  it should "+" in {
    var a = BitNumber(Array(10.toByte))
    var b = BitNumber(Array(8.toByte))

    var res = a + b
    var expect = "00010010"

    assert(res.toBinStr == expect)
    assert(res.size == 8)

    println("----")

    //--------------------------------

    a = BitNumber(Array(152.toByte))
    b = BitNumber(Array(183.toByte))

    res = a + b
    expect = "101001111"

    assert(res.toBinStr == expect)
    assert(res.size == 9)

  }

  /** Добавление */
  it should "b add to a" in {
    var a = BitNumber(Array(10.toByte))
    var b = BitNumber(Array(8.toByte))

    a += b
    var expect = "00010010"

    assert(a.toBinStr == expect)
    assert(a.size == 8)
    assert(b.toBinStr == "00001000")

    println("----")

    //--------------------------------

    a = BitNumber(Array(152.toByte))
    b = BitNumber(Array(183.toByte))

    a += b
    expect = "101001111"

    assert(a.toBinStr == expect)
    assert(a.size == 9)
    assert(b.toBinStr == "10110111")

  }

  /** Умножение */
  it should "multiply" in {
    println("Multiply")

    var a = BitNumber(Array(10.toByte))
    var b = BitNumber(Array(8.toByte))

    var res = a ** b
    var expect = "000000001010000"

    assert(res.toBinStr == expect)
    assert(res.size == 15)

    println("----")

    //--------------------------------

    //TODO еще пример
  }

}
