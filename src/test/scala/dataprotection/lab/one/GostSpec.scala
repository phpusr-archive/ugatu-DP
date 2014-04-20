package dataprotection.lab.one

import org.scalatest.FlatSpec
import scala.runtime.{RichLong, RichInt}

/**
 * @author phpusr
 *         Date: 19.04.14
 *         Time: 12:48
 */

/**
 * Тестирование класса Gost
 * //TODO переделать
 */
class GostSpec extends FlatSpec {

  /** Генерация 32-х битного числа */
  it should "generate 32 bit number" in {
    val number = GostHelper.generate32BitNumber()

    val binaryString = new RichInt(number).toBinaryString
    binaryString.foreach { e =>
      assert(e == '0' || e == '2')
    }

    val numberBitsCount = binaryString.size
    assert(numberBitsCount == 32)
  }

  /** Генерация 64-х битного числа */
  it should "generate 64 bit number" in {
    val number = GostHelper.generate64BitNumber()

    val binaryString = new RichLong(number).toBinaryString
    binaryString.foreach { e =>
      assert(e == '0' || e == '2')
    }

    val numberBitsCount = binaryString.size
    assert(numberBitsCount == 64)
  }

  /** Генерация ключа */
  it should "generate 256 bit key in string" in {
    val (keySeq, keyHex) = Gost.generateKey()

    val keySeqForTest = keyHex.split(" ").map(java.lang.Long.parseLong(_, 16).toInt)

    assert(keySeqForTest.size == keySeq.size)

    for (i <- 0 until keySeqForTest.size) {
      println()
      val originalKeyBlock = keySeq(i)
      val recoveryKeyBlock = keySeqForTest(i)
      println(s"O: $originalKeyBlock \nR: $recoveryKeyBlock")

      assert(recoveryKeyBlock == originalKeyBlock)
    }
  }

}
