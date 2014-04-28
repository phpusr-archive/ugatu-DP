package experiment.gost

import org.bouncycastle.crypto.engines.GOST28147Engine
import org.bouncycastle.crypto.params.KeyParameter
import dataprotection.lab.one.gost2814789.tools.GostHelper
import dataprotection.lab.one.gost2814789.Gost

/**
 * @author phpusr
 *         Date: 27.04.14
 *         Time: 16:49
 */

/**
 * Сравнение моего ГОСТ с библиотечным bouncycastle
 */
object LibTest extends App {

  /** Ключ */
  val key = Array (0x733D2C20, 0x65686573, 0x74746769, 0x79676120, 0x626E7373, 0x20657369, 0x326C6568, 0x33206D54).reverse

  /** 64-битные блоки для шифрования */
  val blocks = Array(1L, 2)
  def blocksString = blocks.mkString(" ")

  val myHex = my()
  val libHex = lib()

  println(s"$myHex == $libHex")
  assert(myHex == libHex)

  /** Шифрование с помощью моего класса */
  def my() = {
    val res = Gost.encryptBlockArray(blocks, key)
    val parts = res.flatMap { e =>
      val left = GostHelper.getLeftPart64BitNumber(e)
      val right = GostHelper.getRightPart64BitNumber(e)
      Array(left, right)
    }
    val partsStr = parts.mkString(" ")
    val hex = GostHelper.blockArrayToHexString(res)
    println(s"$blocksString -> $partsStr ($hex)")

    // decrypt
    val enc = Gost.decryptBlockArray(res, key)
    println(s"dec: ${enc.mkString(" ")}")

    hex
  }

  /** Шифрование с помощью bouncycastle */
  def lib() = {
    val gost = new GOST28147Engine
    val keyParameter = new KeyParameter(keyToToByte(key))

    gost.init(true ,keyParameter)
    val in = blocks.map(longToBytes)

    val encBlocks: Array[Array[Byte]] = in.map { e =>
      val out = new Array[Byte](e.size)
      gost.processBlock(e, 0, out, 0)
      out // На выходе сначало младшая часть, потом старшая
    }
    val res: Array[Array[Int]] = encBlocks.map { e =>
      e.sliding(4, 4).map(bytesToint).toArray.reverse
    }
    val hex = res.map(_.map(_.toHexString).mkString).mkString(" ")

    println(s"$blocksString -> ${res.flatMap(e => e).mkString(" ")} ($hex)")

    // decrypt
    gost.init(false, keyParameter)
    val decBlocks: Array[Array[Byte]] = encBlocks.map { e =>
      val dec = new Array[Byte](e.size)
      gost.processBlock(e, 0, dec, 0)
      dec
    }

    val decInt: Array[Int] = decBlocks.flatMap { e =>
      e.sliding(4, 4).map(bytesToint).toArray.reverse
    }
    println(s"dec: ${decInt.mkString(" ")}")

    hex
  }



  /////// Функции помощники для bouncycastle ///////
  
  private def keyToToByte(key: Array[Int]) = key.flatMap(intToBytes)
  
  private def bytesToint(in: Array[Byte]): Int = {
    ((in(3) << 24) & 0xff000000) + ((in(2) << 16) & 0xff0000) + ((in(1) << 8) & 0xff00) + (in(0) & 0xff)
  }

  private def longToBytes(num: Long) = {
    val left = GostHelper.getLeftPart64BitNumber(num)
    val right = GostHelper.getRightPart64BitNumber(num)

    Array(right, left).flatMap(intToBytes)
  }

  private def intToBytes(num: Int) = {
    val out = new Array[Byte](4)
    out(3) = (num >>> 24).asInstanceOf[Byte]
    out(2) = (num >>> 16).asInstanceOf[Byte]
    out(1) = (num >>> 8).asInstanceOf[Byte]
    out(0) = num.asInstanceOf[Byte]
    
    out
  }
}
