package experiment.gost

import org.bouncycastle.crypto.engines.GOST28147Engine
import scala.runtime.RichInt
import experiment.gost.wasm.Example
import java.nio.charset.StandardCharsets
import dataprotection.lab.one.gost2814789.Gost
import dataprotection.lab.one.gost2814789.tools.GostHelper
import GostHelper._

/**
 * @author phpusr
 *         Date: 13.04.14
 *         Time: 19:24
 */

/**
 * Прототипный код помогающий написанию алгоритма шифрования ГОСТ-28147-89
 */
object TestGost extends App {

  /** Генерацмя Scala-кода S-блока из Java-кода */
  def generateScalaSBlocks() {
    val sBox = GOST28147Engine.getSBox("Default")
    println("Array(")
    var i = 0
    for (s <- sBox) {
      i += 1

      if (i % 16 == 1) print("\tArray[Byte](") else print(",")
      val richS = new RichInt(s)
      print(s"0x${richS.toHexString.toUpperCase}")
      if (i % 16 == 0) println("),")
    }
    println(")")
  }

  /** Выводит коды символов */
  def printNumChars() {
    val Charset = StandardCharsets.UTF_8
    //val Charset = "cp1251"
    val str = "azая"

    val bins = str.getBytes(Charset).map {e =>
      val bin = Example.toBin(e.toInt)
      s"$e -> $bin (${bin.size})"
    }

    bins.foreach(println)
  }

  /** Тестирует шифрование и дешифрование указанное кол-во раз */
  def testEncryptdDecrypt(times: Int) {
    val times = 10
    val blockCount = 10

    for (i <- 1 to times) {
      val (keySeq, keyHex) = GostHelper.generateKey()
      val keyArray = keyHexToKeyArray(keyHex)
      val blockArray = (for (blockIndex <- 1 to blockCount) yield GostHelper.generate64BitNumber()).toArray

      val enc = Gost.encryptBlockArray(blockArray, keyArray)
      val dec = Gost.decryptBlockArray(enc, keyArray)

      println("\n>> it should equals before encryption and after decryption")
      println("blk = " + blockArray.mkString(" "))
      println("enc = " + enc.mkString(" "))
      println("dec = " + dec.mkString(" "))

      assert(blockArray.mkString("") == dec.mkString(""))
    }
  }

  //printNumChars()
  testEncryptdDecrypt(10)


}
