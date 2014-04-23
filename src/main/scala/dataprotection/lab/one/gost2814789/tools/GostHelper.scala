package dataprotection.lab.one.gost2814789.tools

import scala.util.Random
import dataprotection.lab.one.gost2814789.GostConstants._
import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer
import dataprotection.lab.one.block.Block

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

  /** Разделитель блоков при выводе ключа */
  val BlockSplitter = " "

  /** Система счисления для вывода ключа */
  val KeyOutputNotation = 16

  /** Кол-во битов для сдвига */
  val ShiftBits = BlockPartSize

  /** Кол-во hex-символов для предоставления 32-битного числа */
  val HexSymbolsCount = 8

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

    val keyHex = keySeq.map(e => Integer.toHexString(e)).mkString(BlockSplitter)

    logger.title("Generate GOST-28147-89 Key")
    logger.debug("keySeq: " + keySeq.mkString(" "))
    logger.debug("keyHex: "+ keyHex)

    (keySeq, keyHex)
  }

  /**
   * Преобразование введенного ключа из 16-ной строки в 10-ный массив <br/>
   *
   * Long потому что число выходит за рамки Int (использует все 32 бита) <br/>
   * //TODO hex-блоки фиксированного размера, можно не использовать пробелы
   */
  def keyHexToKeyArray = (keyHex: String) => {
    keyHex.split(BlockSplitter).map(java.lang.Long.parseLong(_, KeyOutputNotation).toInt)
  }

  /**
   * Преобразование массива 64-битных блоков в строку 16-х чисел <br/>
   *
   * Число разбивается на две 32-битыне части <br/>
   * Каждая часть преобразуется в 16-ю строку <br/>
   *
   * Разбиение сделно, потому что блоки могут занимать все 64 бита <br/>
   * и не конвертироваться назад в Long <br/>
   */
  def blockArrayToHexString = (blockArray: Array[Long]) => {
    // Формат вывода hex-числа (дополняет спереди нулями до 8 символов)
    val Format = s"%${HexSymbolsCount}s"
    blockArray.flatMap{ e =>
      val left = getLeftPart64BitNumber(e)
      val right = getRightPart64BitNumber(e)
      List(left, right).map { e =>
        (e.toHexString formatted Format).replace(' ', '0')
      }
    }.mkString("")
  }

  /**
   * Преобразование строки 16-х чисел в массива 64-битных чисел <br/>
   *
   * Строка первый раз разбивается одним делителем <br/>
   * Потом эти блоки разбиваются второй раз <br/>
   * Каждый блок преобразуется в 32-битное число <br/>
   * Результаты склеиваются <br/>
   */
  def hexStringToBlockArray = (string: String) => {
    // Регулярное выражение для разбивки текста на блоки по 16 символов
    val twoBlocksRegExp = s"(?<=\\G.{${HexSymbolsCount * 2}})"
    // Регулярное выражение для разбивки текста на блоки по 8 символов
    val oneBlockRegExp = s"(?<=\\G.{$HexSymbolsCount})"

    string.split(twoBlocksRegExp).map { e =>
      val parts = e.split(oneBlockRegExp).map { e =>
        java.lang.Long.parseLong(e, KeyOutputNotation).toInt
      }
      Block(parts(0), parts(1)).allParts
    }
  }

  /** Преобразование строки в массив 64-битных блоков */
  //TODO указать чем дополнять незавершенный блок
  def stringToBlockArray = (string: String) => {

    val bytes = string.getBytes(CharsetName)

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
      // Если элемент отрицательный, то они будут мешать
      val eShiftLeft = e.toLong << ((ByteInLongCount - 1) * ByteSize)
      // Добавляемый элемент, сдвинутый от начала на занятое кол-во битов
      val e64 = eShiftLeft >>> buzyBits

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
        // Сдвигаем блок символа до конца влево (избавляемся от нулей в начале)
        val blockShiftLeft = block << (i * ByteSize)

        // Сдвигаем блок символа до конца вправо (избавляемся от нулей в конце)
        val eByte = (blockShiftLeft >>> (ByteInLongCount - 1) * ByteSize).toByte

        // Добавляем блок элемента в список байтов
        byteBuffer += eByte
      }

      byteBuffer
    }

    new String(bytes, CharsetName)
  }

}
