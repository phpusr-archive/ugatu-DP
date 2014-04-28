package experiment.rc4

import dataprotection.lab.two.rc4.RC4
import org.bouncycastle.crypto.engines.RC4Engine
import org.bouncycastle.crypto.params.KeyParameter

/**
 * @author phpusr
 *         Date: 28.04.14
 *         Time: 15:54
 */

/**
 * Сравнение моего RC4 с библиотечным bouncycastle
 */
object LibTest extends App {

  /** Ключ */
  val key = RC4.generateKey(10)
  println(s"key: (${key.mkString(", ")})")

  /** Сообщение для шифрования */
  val message = "Hello, World!"
  val CharsetName = "cp1251"
  val messageBytes = message.getBytes(CharsetName)

  val myRes = my()
  val libRes = lib()

  println(s"($myRes) == ($libRes)")
  assert(myRes == libRes)

  /** Шифрование с помощью моего класса */
  def my() = {
    val enc = RC4.encrypt(messageBytes, key)
    val encString = enc.mkString(" ")
    println(s"my : $encString")

    // Decrypt
    val dec = RC4.decrypt(enc, key)
    val decStr = new String(dec, CharsetName)
    println(s"$message == $decStr")
    assert(message == decStr)

    encString
  }

  /** Шифрование с помощью bouncycastle */
  def lib() = {
    val keyParameter = new KeyParameter(key)
    val rc4 = new RC4Engine()
    rc4.init(true, keyParameter)

    val enc = new Array[Byte](messageBytes.size)
    rc4.processBytes(messageBytes, 0, messageBytes.size, enc, 0)
    val encString = enc.mkString(" ")
    println(s"lib: $encString")

    // Decrypt
    rc4.reset()
    val dec = new Array[Byte](enc.size)
    rc4.processBytes(enc, 0, enc.size, dec, 0)
    val decStr = new String(dec, CharsetName)
    println(s"$message == $decStr")
    assert(message == decStr)

    encString
  }
  
}
