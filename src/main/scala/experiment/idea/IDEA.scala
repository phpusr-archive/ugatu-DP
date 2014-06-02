package experiment.idea

import org.dyndns.phpusr.bitnumber.BitNumber
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 31.05.14
 *         Time: 17:06
 */

object IDEA {

  /** Размер ключа */
  val KeySize = 128
  /** Размер частей ключа */
  val KeyPartSize = 16
  /** Кол-во подключей */
  val SubKeysCount = 52
  /** Кол-во блоков подключей */
  val SubKeysBlocksCount = 6

  /** Размер блока */
  val BlockSize = 64
  /** Кол-во подблоков */
  val SubBlocksCount = 4
  /** Размер подблоков */
  val SubBlocksSize = 16

  /** Отладка */
  def main(args: Array[String]) {
    val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
    val key = BitNumber(keyArray)
    println(s"key: ${key.toHexStr}")

    val dataArray = Array(0, 0, 0, 1, 0, 2, 0, 3).map(_.toByte)
    val data = BitNumber(dataArray)

    val idea = new IDEA(key, true)
    println(idea.subKeysStr + "\n")
    val enc = idea.processBlocks(data)
    println("enc: " + enc.toHexStr)

    val ideaDycrypt = new IDEA(key, false)
    println(ideaDycrypt.subKeysStr + "\n")

    val dec = ideaDycrypt.processBlocks(enc)
    println("dec: " + dec.toHexStr)

    assert(data.toString == dec.toString)
  }

}

/**
 * Алгоритм шифрования данных IDEA
 * http://ru.wikipedia.org/wiki/IDEA
 */
//TODO println
class IDEA(key: BitNumber, encrypt: Boolean) extends IDEATools {
  
  import experiment.idea.IDEA._

  assert(key.size == KeySize)

  /** Подключи */
  private val subKeys = if (encrypt) generateSubKeys else invertKey(generateSubKeys)
  assert(subKeys.map(_.size).sum == SubKeysCount)

  /** Подключи в виде строки (для отлдаки) */
  val subKeysStr = subKeys.map{ e =>
    e.map(_.toHexStr).mkString(", ")
  }.mkString("\n")

  /** Шифрование данных */
  def processBlocks(data: BitNumber) = {
    val out = BitNumber(0)
    data.split(BlockSize).map(processBlock).foreach(out.join)
    out
  }

  /** Генерация подключей */
  private def generateSubKeys = {
    val tmpSubKeys = ListBuffer[BitNumber]()
    var shiftKey = key
    for (i <- 1 to SubKeysBlocksCount) {
      tmpSubKeys ++= shiftKey.split(KeyPartSize)
      shiftKey = shiftKey << 25
    }
    val sk = shiftKey.split(KeyPartSize)
    tmpSubKeys ++= sk.take(4)

    // Разбитие на блоки по 6 подключей
    tmpSubKeys.sliding(SubKeysBlocksCount, SubKeysBlocksCount).toList
  }

  /** Шифрование/расшифрование блока */
  private def processBlock(data: BitNumber) = {
    assert(data.size == BlockSize)

    var d = data.split(SubBlocksSize)
    println(s"0. " + d.map(_.toHexStr).mkString(", "))
    assert(d.size == SubBlocksCount)

    /** Выделение последних 16 бит */
    val m = (bitNumber: BitNumber) => bitNumber.last(SubBlocksSize)

    for (i <- 0 until 8) {
      val k = subKeys(i)

      val s1 = mul(d(0), k(0))
      val s2 = m(d(1) + k(1))
      val s3 = m(d(2) + k(2))
      val s4 = mul(d(3), k(3))
      val s5 = s1 xor s3
      val s6 = s2 xor s4

      val s7 = mul(s5, k(4))
      val s8 = m(s6 + s7)
      val s9 = mul(s8, k(5))
      val s10 = m(s7 + s9)

      val d0 = s1 xor s9
      val d1 = s3 xor s9
      val d2 = s2 xor s10
      val d3 = s4 xor s10

      d = List(d0, d1, d2, d3)

      println(s"${i+1}. " + d.map(_.toHexStr).mkString(", "))
    }

    val k = subKeys(8)
    val d0 = mul(d(0), k(0))
    val d1 = d(2) + k(1)
    val d2 = d(1) + k(2)
    val d3 = mul(d(3), k(3))

    //TODO improve
    val res = List(d0, d1, d2, d3).map(m)
    val r = BitNumber(0)
    res.foreach(r.join)

    r
  }

}
