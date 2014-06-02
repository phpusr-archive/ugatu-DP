package dataprotection.rgr.idea

import org.scalatest.FlatSpec
import org.dyndns.phpusr.bitnumber.BitNumber
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.engines.IDEAEngine

/**
 * @author phpusr
 *         Date: 02.06.14
 *         Time: 13:45
 */

/**
 * Тестирование IDEA
 */
class IDEA$Spec extends FlatSpec {

  /** Преобразование BitNumber в удобочитаемый вид */
  val toHex = (number: BitNumber) => number.toHexStr.sliding(4, 4).mkString(", ")

  /** Отладка шифрования и расшифрования */
  it should "debug" in {

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

  /** Сравнение с библиотечным */
  it should "equals with lib" in {
    println("\nEquals with lib")

    println("\nEncrypt process:")

    // Шифрование библиотекой
    val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
    val keyParam = new KeyParameter(keyArray)

    val engine = new IDEAEngine
    engine.init(true, keyParam)

    val dataArray = Array(0, 0, 0, 1, 0, 2, 0, 3).map(_.toByte)
    val encryptArray = new Array[Byte](dataArray.size)
    engine.processBlock(dataArray, 0, encryptArray, 0)
    val encryptBitsLib = BitNumber(encryptArray)
    println("lib: " + toHex(encryptBitsLib))

    // Шифрование моим классом
    val key = BitNumber(keyArray)
    val idea = new IDEA(key, true)
    val data = BitNumber(dataArray)
    val encryptBits = idea.processBlocks(data)
    println("my : " + toHex(encryptBits))

    assert(encryptBitsLib == encryptBits)

    println("\nDecrypt process:")

    // Расшифрование библиотекой
    engine.init(false, keyParam)
    val decryptArray = new Array[Byte](dataArray.size)
    engine.processBlock(encryptArray, 0, decryptArray, 0)
    val decryptBitsLib = BitNumber(decryptArray)
    println("lib: " + toHex(decryptBitsLib))

    // Расшифрование моим классом
    val decryptIdea = new IDEA(key, false)
    val decryptBits = decryptIdea.processBlocks(encryptBits)
    println("my : " + toHex(decryptBits))

    assert(decryptBitsLib == decryptBits)
    assert(decryptBits == data)
  }

  /** Проверка генерации ключа */
  it should "generate key" in {
    println("\nGenerate key:")

    val key = IDEA.generateKey()
    println(s"key bin: $key")
    println(s"key hex: ${key.toHexStr}")
    assert(key.size == 128)
  }

}
