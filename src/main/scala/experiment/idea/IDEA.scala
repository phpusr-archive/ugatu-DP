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

  /** Отладка */
  def main(args: Array[String]) {
    val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
    val key = BitNumber(keyArray)
    println(s"key: ${key.toHexStr}")

    generateSubKeys(key)

    val str = subKeys.map{ e =>
      e.map(_.toHexStr)
    }.mkString("\n")
    println(str)
  }

}
