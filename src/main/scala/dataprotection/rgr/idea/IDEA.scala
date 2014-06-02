package dataprotection.rgr.idea

import org.dyndns.phpusr.bitnumber.BitNumber
import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * @author phpusr
 *         Date: 31.05.14
 *         Time: 17:06
 */

object IDEA {

  /** Отладка */
  val DebugEnable = false

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

  /** Генерация ключа */
  def generateKey() = {
    val key = BitNumber(0)
    for (i <- 1 to 4) {
      val part = BitNumber(Array(Random.nextInt()))
      key.join(part)
    }

    key
  }

}

/**
 * Алгоритм шифрования данных IDEA
 * http://ru.wikipedia.org/wiki/IDEA
 */
class IDEA(key: BitNumber, encrypt: Boolean) extends IDEATools {

  import dataprotection.rgr.idea.IDEA._

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
    if (DebugEnable) println(s"0. " + d.map(_.toHexStr).mkString(", "))
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

      if (DebugEnable) println(s"${i+1}. " + d.map(_.toHexStr).mkString(", "))
    }

    val k = subKeys(8)
    val d0 = mul(d(0), k(0))
    val d1 = d(2) + k(1)
    val d2 = d(1) + k(2)
    val d3 = mul(d(3), k(3))

    // Склеивание подблоков
    val dList = List(d0, d1, d2, d3).map(m)
    val outBits = BitNumber(0)
    dList.foreach(outBits.join)

    outBits
  }

}
