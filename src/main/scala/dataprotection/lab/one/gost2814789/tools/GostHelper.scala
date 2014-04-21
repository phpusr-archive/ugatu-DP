package dataprotection.lab.one.gost2814789.tools

import scala.util.Random
import dataprotection.lab.one.gost2814789.GostConstants._
import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer

/**
 * @author phpusr
 *         Date: 20.04.14
 *         Time: 16:13
 */

/**
 * Объект с функциями помогающими шифрованию ГОСТ-28147-89
 */
object GostHelper {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

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

    logger.title("Generate GOST-28147-89 Key")
    logger.debug("keySeq: " + keySeq.mkString(" "))
    logger.debug("keyHex: "+ keyHex)

    (keySeq, keyHex)
  }

  /** Преобразование введенного ключа из 16-ной строки в 10-ный массив */
  def keyHexToKeyArray = (keyHex: String) => {
    keyHex.split(KeySplitter).map(java.lang.Long.parseLong(_, KeyOutputNotation).toInt)
  }

  /** Преобразование строки в массив 64-битных блоков */
  //TODO указать чем дополнять незавершенный блок
  //TODO Попробовать с другой кодировкой
  def stringToBlockArray = (message: String) => {

    val bytes = message.getBytes(CharsetName)

    val blockBuffer = ListBuffer[Long]()

    // Текущее кол-во байтов в блоке
    var byteInBlockIndex = 0
    // Текущий обрабатываемый блок
    var currentBlock = 0L
    // Текущее кол-во блоков
    var currentBlockCount = 0

    bytes.foreach { e =>
      byteInBlockIndex += 1

      // Количество занятых бит в блоке
      val buzyBits = ByteSize * (byteInBlockIndex-1)
      // Стираем начальные 56 битов, которые не относятся к элементу
      // Если элемент отрицательный, то они будут мешать //TODO норм имена для переменных
      val el = e.toLong << ((ByteInLongCount - 1) * ByteSize)
      // Добавляемый элемент, сдвинутый от начала на занятое кол-во битов
      val e64 = el >>> buzyBits

      // Добавление элемента в блок
      currentBlock = currentBlock | e64

      // Если блок заполнен
      if (byteInBlockIndex % ByteInLongCount == 0) {
        byteInBlockIndex = 0
        currentBlockCount += 1
        blockBuffer += currentBlock
        currentBlock = 0L
      }
    }
    // Если последний блок не был заполнен до конца, добавить его
    if (byteInBlockIndex != 0) blockBuffer += currentBlock

    blockBuffer.toArray
  }

  /** Преобразование 64-битных блоков назад в строку */
  //TODO учитывать незавершенные блоки
  def blockArrayToString = (blockArray: Array[Long]) => {

    val bytes = blockArray.flatMap { block =>
      val byteBuffer = ListBuffer[Byte]()

      for (i <- 0 until ByteInLongCount) {
        //TODO норм имена для переменных
        // Сдвигаем блок символа до конца влево (избавляемся от нулей в начале)
        val el = block << (i * ByteSize)

        // Сдвигаем блок символа до конца вправо (избавляемся от нулей в конце)
        val e = (el >>> (ByteInLongCount - 1) * ByteSize).toByte

        // Добавляем блок элемента в список байтов
        byteBuffer += e
      }

      byteBuffer
    }

    new String(bytes, CharsetName)
  }

}
