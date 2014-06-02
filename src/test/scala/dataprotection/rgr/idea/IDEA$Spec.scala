package dataprotection.rgr.idea

import org.scalatest.FlatSpec
import org.dyndns.phpusr.bitnumber.BitNumber

/**
 * @author phpusr
 *         Date: 02.06.14
 *         Time: 13:45
 */

/**
 * Тестирование IDEA
 */
class IDEA$Spec extends FlatSpec {

  /** Отладка шифрования и расшифрования */
  it should "debug" in {

    val toHex = (number: BitNumber) => number.toHexStr.sliding(4, 4).mkString(", ")

    val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
    val key = BitNumber(keyArray)
    println(s"key: ${toHex(key)}")

    val dataArray = Array(0, 0, 0, 1, 0, 2, 0, 3).map(_.toByte)
    val data = BitNumber(dataArray)
    println(s"\ndata: ${toHex(data)}")

    val idea = new IDEA(key, true)
    println("\nEncrypt key:\n" + idea.subKeysStr)
    println("\nEncrypt process:")
    val enc = idea.processBlocks(data)
    println("Encrypt data: " + toHex(enc))

    val ideaDycrypt = new IDEA(key, false)
    println("\nDecrypt key:\n" + ideaDycrypt.subKeysStr)
    println("\nDecrypt process:")
    val dec = ideaDycrypt.processBlocks(enc)
    println("Decrypt data: " + toHex(dec))

    assert(data.toString == dec.toString)

  }

}
