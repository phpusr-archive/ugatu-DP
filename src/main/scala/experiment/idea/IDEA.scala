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
  /** Размер блока */
  val BlockSize = 64

  /** Подключи */
  private val subKeys = ListBuffer[BitNumber]()

  /** Шифрование данных */
  def encrypt(data: BitNumber, key: BitNumber) = {
    assert(data.size == BlockSize)
    assert(key.size == KeySize)

    generateSubKeys(key)
    assert(subKeys.size == SubKeysCount)
  }

  /** Генерация подключей */
  def generateSubKeys(key: BitNumber) {
    var shiftKey = key
    for (i <- 1 to 6) {
      subKeys ++= shiftKey.split(KeyPartSize)
      shiftKey = shiftKey << 25
    }
    val sk = shiftKey.split(KeyPartSize)
    subKeys ++= sk.take(4)
    //TODO возможно стоит разбить по 6 элементов
  }

  /** Отладка */
  def main(args: Array[String]) {
    val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
    val key = BitNumber(keyArray)
    println(s"key: ${key.toHexStr}")

    generateSubKeys(key)

    val str = subKeys.map(_.toHexStr).sliding(6, 6).map { e =>
      e.mkString(", ")
    }.mkString("\n")
    println(str)
  }

}
