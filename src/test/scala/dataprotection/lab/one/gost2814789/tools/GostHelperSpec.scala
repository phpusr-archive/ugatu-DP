package dataprotection.lab.one.gost2814789.tools

import org.scalatest.FlatSpec

/**
 * @author phpusr
 *         Date: 19.04.14
 *         Time: 12:48
 */

/**
 * Тестирование класса GostHelper
 */
class GostHelperSpec extends FlatSpec {

  /** Генерация 32-х битного числа */
  it should "generate 32 bit number" in {
    val number = GostHelper.generate32BitNumber()

    val binaryString = number.toBinaryString
    binaryString.foreach { e =>
      assert(e == '0' || e == '1')
    }

    val numberBitsCount = binaryString.size
    assert(numberBitsCount == 32)
  }

  /** Генерация 64-х битного числа */
  it should "generate 64 bit number" in {
    val number = GostHelper.generate64BitNumber()

    val binaryString = number.toBinaryString
    binaryString.foreach { e =>
      assert(e == '0' || e == '1')
    }

    val numberBitsCount = binaryString.size
    assert(numberBitsCount == 64)
  }

  /** Генерация ключа */
  it should "generate 256 bit key in string" in {
    val (keySeq, keyHex) = GostHelper.generateKey()

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

  /** Преобразование строки в массив 64-битных блоков */
  it should "converted string to 64-bits blokcs" in {
    val message = "abcdsdjfhsjdkhf"
    val messageBinaryArray = message.getBytes.map(_.toInt.toBinaryString)
    println("\nbytes: " + messageBinaryArray.mkString(" "))

    // Массив блоков по 64 бита
    val blockArray = GostHelper.stringToBlockArray(message)

    val binaryBlockArray = blockArray.map(_.toBinaryString)
    val subBlockArray = binaryBlockArray.flatMap { e =>
      // Разделение строки на блоки по 8 символов (с конца)
      val blockBytes = e.reverse.split(s"(?<=\\G.{8})").map(_.reverse).reverse

      // Удаляем начальнык нули у подблоков
      val tmp = blockBytes.map { sub =>
        var changeSub = sub
        while (changeSub.size > 1 && changeSub.charAt(0) == '0') changeSub = changeSub.substring(1)
        changeSub
      }
      println("block: " + tmp.mkString(" "))

      tmp
    }

    // Проверка введеной строки и преобразованных блоков
    for (i <- 0 until messageBinaryArray.size) {
      println(s"${messageBinaryArray(i)} == ${subBlockArray(i)}")
      assert(messageBinaryArray(i) == subBlockArray(i))
    }

    println("\nbytes: " + messageBinaryArray.mkString(" "))
    println("reslt: " + subBlockArray.mkString(" "))

    //TODO отдельный тест
    val backMessage = GostHelper.blockArrayToString(blockArray)
    println(s"backMessage: '$backMessage'")
    assert(message == backMessage.substring(0, message.size))
  }

  /** Преобразование 64-битных блоков назад в строку */


}
