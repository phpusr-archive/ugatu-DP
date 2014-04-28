package dataprotection.lab.two.rc4

import org.scalatest.FlatSpec

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 14:20
 */

/**
 * Тестирование класса RC4
 */
class RC4Spec extends FlatSpec {

  /** Должен зашифровать и расшифровать, результаты должны совпасть */
  it should "encrypt and decrypt" in {
    val CharsetName = "cp1251"
    val key = Array[Byte](0, 0, 0, 0)
    val data = "test"
    val bytes = data.getBytes(CharsetName)

    // Шифрование
    val enc = RC4.encrypt(bytes, key)
    println("\nenc: " + enc.mkString(" "))

    // Расшифрование
    val dec = RC4.decrypt(enc, key)
    val decryptData = new String(dec, CharsetName)

    println("\n" + bytes.mkString(" ") + " == " + dec.mkString(" "))
    println("dec: " + decryptData)
    assert(decryptData == data)
  }

  /** Зашифрованные результаты должны совпать с эталонными (взяты из библиотеки bouncycastle) */
  it should "encrypt result coincide with reference" in {
    val message = "Hello, World!"
    val key = Array(114, 61, 12, -98, 13, 78, 110, -8, 111, 36).map(_.toByte)

    val enc = RC4.encrypt(message.getBytes("cp1251"), key)
    val encStr = enc.mkString(" ")

    val reference = "-119 103 -92 -114 -30 -83 7 -55 78 8 -102 67 47"
    println(s"($encStr) == ($reference)")

    assert(encStr == reference)
  }

}
