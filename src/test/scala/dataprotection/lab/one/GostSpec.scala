package dataprotection.lab.one

import org.scalatest.FlatSpec
import dataprotection.lab.one.GostHelper._

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 10:20
 */

/**
 * Тестирование класса Gost
 */
class GostSpec extends FlatSpec {

  /** Должна пройзоти ошибка неправильного размера ключа */
  it should "throw IllegalArgumentException if keyArray have improper size" in {
    intercept [IllegalArgumentException] {
      Gost.decryptBlock(0, Array())
    }

    intercept [IllegalArgumentException] {
      Gost.encryptBlock(0, Array())
    }
  }

  /** Не должно произойти ошибки размера ключа */
  it should "executed without exceptions" in {
    Gost.encryptBlock(-1, Array(1, 2, 3, 4, 5, 6, 7, 8))
  }

  /** Должен зашифровать и расшифровать блок, результаты должны совпасть */
  it should "equals before encryption and after decryption" in {
    val times = 10

    for (i <- 1 to times) {
      val block = GostHelper.generate64BitNumber()
      val (keySeq, keyHex) = GostHelper.generateKey()
      val keyArray = keyHexToKeyArray(keyHex)

      val enc = Gost.encryptBlock(block, keyArray)
      val dec = Gost.decryptBlock(enc, keyArray)

      println("\nblk = " + block.toBinaryString)
      println("enc = " + enc.toBinaryString)
      println("dec = " + dec.toBinaryString)

      assert(block == dec)
    }
  }


}
