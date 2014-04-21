package dataprotection.lab.one.gost2814789.tools

import scala.util.Random
import dataprotection.lab.one.gost2814789.GostConstants._

/**
 * @author phpusr
 *         Date: 20.04.14
 *         Time: 16:13
 */

/**
 * Объект с функциями помогающими шифрованию ГОСТ-28147-89
 */
object GostHelper {

  /** Кол-во битов для сдвига */
  val ShiftBits = BlockPartSize
  
  /** Генерирует 32-битное число */
  def generate32BitNumber = () => Random.nextInt().abs * -1

  /** Генерирует 64-битное число */
  def generate64BitNumber = () => Random.nextLong().abs * -1

  /** Возвращает левую часть 64-битного числа */
  def getLeftPart64BitNumber = (block: Long) => (block >>> ShiftBits).toInt

  /** Возвращает правую часть 64-битного числа */
  def getRightPart64BitNumber = (block: Long) => (block << ShiftBits >>> ShiftBits).toInt

  /** Генерация 256-битного ключа */
  def generateKey = () => {

    // Список из 8-ми 32-х битных частей ключа
    val keySeq = for (i <- 1 to KeyBlocksCount) yield generate32BitNumber()

    val keyHex = keySeq.map(e => Integer.toHexString(e)).mkString(KeySplitter)

    (keySeq, keyHex)
  }

  /** Преобразование введенного ключа из 16-ной строки в 10-ный массив */
  def keyHexToKeyArray = (keyHex: String) => {
    keyHex.split(KeySplitter).map(java.lang.Long.parseLong(_, KeyOutputNotation).toInt)
  }

}
