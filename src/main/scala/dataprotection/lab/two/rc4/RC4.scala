package dataprotection.lab.two.rc4

import org.dyndns.phpusr.util.log.Logger
import dataprotection.lab.two.counter.Counter
import scala.util.Random

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 13:20
 */

/**
 * Шифрование RC4 <br/>
 * <br/>
 * Источники: <br/>
 * <u><a href="http://ru.wikipedia.org/wiki/RC4">Статья в Википедии</a><br/><u/>
 * <u><a href="http://ru.wikibooks.org/w/index.php?oldid=66041">Программные реализации RC4</a></u>
 */
object RC4 {

  /** Тип для S-блока */
  private type SBlocType = Int

  /** Тип для элементов шифрования */
  private type RC4Type = Byte

  /** Размер S-блока */
  private val SBlockSize = 256

  /** Разделитель блоков */
  val Splitter = " "

  /** Кодировка для преобразования строки */
  val CharsetName = "cp1251"

  private val logger = new Logger(true, true, false)

  //------------------------------------------

  /** Генератор ключа */
  def generateKey = (size: Int) => {
    (for (i <- 1 to size) yield Random.nextInt().toByte).toArray
  }

  /** Инициализация S-блока (Key-Scheduling Algorithm) */
  private def ksa(key: Array[RC4Type]) = {
    val keyArray = key.map(rc4eToSBlock)
    val sBlock = (0 until SBlockSize).toArray

    var j = 0
    for (i <- 0 until SBlockSize) {
      j = (j + sBlock(i) + keyArray(i % keyArray.size)) % SBlockSize
      // Меняются метсами
      swap(sBlock, i, j)
    }

    logger.title("Generate S-block")
    logger.trace(sBlock.mkString(" "))

    sBlock
  }

  /** Шифрование */
  def encrypt(data: Array[RC4Type], key: Array[RC4Type]) = encryptAndDecrypt(data, key)
  /** Расшифрование */
  def decrypt(encryptData: Array[RC4Type], key: Array[RC4Type]) = encryptAndDecrypt(encryptData, key)

  /** Шифрование / Расшифрование */
  private def encryptAndDecrypt(data: Array[RC4Type], key: Array[RC4Type]) = {
    val iCounter = new Counter
    val jCounter = new Counter
    val sBlock = ksa(key)

    data.map { e =>
      val gamma = prga(iCounter, jCounter, sBlock)
      logger.debug("gamma: " + gamma)
      sBlockToRc4(rc4eToSBlock(e) ^ gamma)
    }
  }

  /** Генерация псевдо-случайного числа K (Pseudo-Random Generation Algorithm) */
  private def prga(i: Counter, j: Counter, sBlock: Array[SBlocType]) = {
    logger.trace("iC: " + i)
    logger.trace("jC: " + j)
    i.value = (i.value + 1) % SBlockSize
    j.value = (j.value + sBlock(i.value)) % SBlockSize

    // Меняются местами
    swap(sBlock, i.value, j.value)
    val t = (sBlock(i.value) + sBlock(j.value)) % SBlockSize
    sBlock(t)
  }

  /** RC4Type -> SBlockType */
  private def rc4eToSBlock = (num: RC4Type) => {
    val ShiftBits = 24
    num << ShiftBits >>> ShiftBits
  }

  /** SBlockType -> RC4Type */
  private def sBlockToRc4 = (num: SBlocType) => num.toByte

  /** Изменение мест элементов в массиве */
  private def swap(array: Array[SBlocType], i: Int, j: Int) {
    val oldSi = array(i)
    array(i) = array(j)
    array(j) = oldSi
  }

}
