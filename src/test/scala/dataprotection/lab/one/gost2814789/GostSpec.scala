package dataprotection.lab.one.gost2814789

import org.scalatest.FlatSpec
import dataprotection.lab.one.gost2814789.tools.GostHelper
import GostHelper._

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 10:20
 */

/**
 * Тестирование объекта Gost
 */
class GostSpec extends FlatSpec {

  /** Должна пройзоти ошибка неправильного размера ключа */
  it should "throw IllegalArgumentException if keyArray have improper size" in {
    intercept [IllegalArgumentException] {
      Gost.decryptBlockArray(Array(0L), Array())
    }

    intercept [IllegalArgumentException] {
      Gost.encryptBlockArray(Array(0L), Array())
    }
  }

  /** Не должно произойти ошибки размера ключа */
  it should "executed without exceptions" in {
    Gost.encryptBlockArray(Array(0L), Array(1, 2, 3, 4, 5, 6, 7, 8))
  }

  /** Должен зашифровать и расшифровать блок, результаты должны совпасть */
  it should "equals before encryption and after decryption" in {
    val times = 10
    val blockCount = 10

    for (i <- 1 to times) {
      val (keySeq, keyHex) = GostHelper.generateKey()
      val keyArray = keyHexToKeyArray(keyHex)
      val blockArray = (for (blockIndex <- 1 to blockCount) yield GostHelper.generate64BitNumber()).toArray

      val enc = Gost.encryptBlockArray(blockArray, keyArray)
      val dec = Gost.decryptBlockArray(enc, keyArray)

      println("\n>> it should equals before encryption and after decryption")
      if (false) {
        println("blk = " + blockArray.mkString(" "))
        println("enc = " + enc.mkString(" "))
        println("dec = " + dec.mkString(" "))
      }

      assert(blockArray.mkString("") == dec.mkString(""))
    }
  }

  /**
   * Выполнение данного примера должно вернуть
   * 42abbcce 32bc0b1b
   * Что соответствует результату из ГОСТ 34.11-94 (Приложения А)
   */
  it should "test case" in {
    val key = Array(0x733D2C20, 0x65686573, 0x74746769, 0x79676120, 0x626E7373, 0x20657369, 0x326C6568, 0x33206D54).reverse

    val blockArray = Array(0L)

    val enc = Gost.encryptBlockArray(blockArray, key)
    val hexStr = GostHelper.blockArrayToHexString(enc)
    println(s"\nhex: ${hexStr.splitAt(8)} == (42abbcce,32bc0b1b)")
    assert(hexStr == "42abbcce32bc0b1b")

    val dec = Gost.decryptBlockArray(enc, key)
    println(s"dec: ${dec.mkString(" ")} == 0")
    assert(dec.mkString == "0")
  }


}
