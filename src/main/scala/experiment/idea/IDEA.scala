package experiment.idea

import org.dyndns.phpusr.bitnumber.BitNumber
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 31.05.14
 *         Time: 17:06
 */

/**
 * Алгоритм шифрования данных IDEA
 * http://ru.wikipedia.org/wiki/IDEA
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

  /** Подключи */
  private var subKeys: Seq[Seq[BitNumber]] = null

  /** Шифрование данных */
  def encrypt(data: BitNumber, key: BitNumber) = {
    assert(data.size == BlockSize)
    assert(key.size == KeySize)

    // Генерация подключей
    generateSubKeys(key)
    assert(subKeys.size == SubKeysCount)

    // TODO

  }

  /** Генерация подключей */
  def generateSubKeys(key: BitNumber) {
    val tmpSubKeys = ListBuffer[BitNumber]()
    var shiftKey = key
    for (i <- 1 to SubKeysBlocksCount) {
      tmpSubKeys ++= shiftKey.split(KeyPartSize)
      shiftKey = shiftKey << 25
    }
    val sk = shiftKey.split(KeyPartSize)
    tmpSubKeys ++= sk.take(4)

    // Разбитие на блоки по 6 подключей
    subKeys = tmpSubKeys.sliding(SubKeysBlocksCount, SubKeysBlocksCount).toList
  }

  /** TODO */
  def action(data: BitNumber) = {
    var d = data.split(SubBlocksSize)
    println(s"0. " + d.map(_.toHexStr).mkString(", "))
    assert(d.size == SubBlocksCount)

    // TODO умножение по модулю 2^16 + 1, вместо нуля используется 2^16
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

    List(d0, d1, d2, d3).map(m)
  }

  /** Умножение по модулю 2 в 16 + 1 */
  def mul(a: BitNumber, b: BitNumber) = {
    val Base = 0x10001
    val Mask = 0xffff

    var x = a.toInt
    var y = b.toInt

    if (x == 0) x = Base - y
    else if (y == 0) x = Base - x
    else {
      val p = x * y
      y = p & Mask
      x = p >>> 16
      x = y - x + (if (y < x)  1 else 0)
    }

    val res = BitNumber(Array(x & Mask))
    res.last(SubBlocksSize)
  }

  /** Отладка */
  def main(args: Array[String]) {
    val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
    val key = BitNumber(keyArray)
    println(s"key: ${key.toHexStr}")

    generateSubKeys(key)

    val str = subKeys.map{ e =>
      e.map(_.toHexStr)
    }.mkString("\n")
    println(str + "\n")

    val dataArray = Array(0, 0, 0, 1, 0, 2, 0, 3).map(_.toByte)
    val data = BitNumber(dataArray)
    val a = action(data)

    println(a.map(_.toHexStr).mkString(", "))
  }

}
