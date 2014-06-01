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

    val m = (bitNumber: BitNumber) => bitNumber.last(SubBlocksSize)

    for (i <- 0 until 8) {
      val k = subKeys(i)

      val a = m(d(0) * k(0))
      val b = m(d(1) + k(1))
      val c = m(d(2) + k(2))
      val g = m(d(3) * k(3))
      val e = a xor c
      val f = b xor g

      val h = (f + e * k(4)) * k(5)
      val j = e * k(4)

      val d0 = a xor m(h)
      val d1 = c xor m(h)
      val d2 = b xor m(j + h)
      val d3 = g xor m(j + h)

      d = List(d0, d1, d2, d3)

      println(s"${i+1}. " + d.map(_.toHexStr).mkString(", "))
    }

    val k = subKeys(8)
    val d0 = d(0) * k(0)
    val d1 = d(2) + k(1)
    val d2 = d(1) + k(2)
    val d3 = d(3) * k(3)

    List(d0, d1, d2, d3).map(m)
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
    val a = action(data) //TODO fix

    println(a.map(_.toHexStr).mkString(", "))
  }

}
