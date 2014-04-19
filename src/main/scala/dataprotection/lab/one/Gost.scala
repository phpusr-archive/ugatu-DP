package dataprotection.lab.one

import scala.util.Random
import org.dyndns.phpusr.util.log.Logger

/**
 * @author phpusr
 *         Date: 18.04.14
 *         Time: 15:25
 */

/**
 * TODO
 */
object Gost {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Система счисления для вывода ключа */
  private val KeyOutputNotation = 16

  /** Разделитель блоков при выводе ключа */
  private val KeySplitter = " "

  //---------------------------------------------//

  /** Генерирует 32-битное число */
  def generate32BitNumber = () => Random.nextInt().abs * -1

  /** Генерирует 64-битное число */
  def generate64BitNumber = () => Random.nextLong().abs * -1

  /** Генерирует 256-битный ключ */
  def generateKey = () => {
    val KeyBlocksCount = 8

    // Список из 8-ми 32-х битных частей ключа
    val keySeq = for (i <- 1 to KeyBlocksCount) yield generate32BitNumber()

    val keyHex = keySeq.map(e => Integer.toHexString(e)).mkString(KeySplitter)
    logger.debug("Key hex: " + keyHex)

    (keySeq, keyHex)
  }

  /** Преобразование введенного ключа из 16-й строки в 10-й массив */
  def keyHexToKeyArray = (keyHex: String) => {
    keyHex.split(KeySplitter).map(java.lang.Long.parseLong(_, KeyOutputNotation).toInt)
  }

}

class Gost(keyHexString: String) {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Ключ */
  private val key = Gost.keyHexToKeyArray(keyHexString)

  /** Размер блока шифрования в битах */
  private val BlockPartSize = 32

  //---------------------------------------------//

  /** Основной шаг криптопреобразования */
  private def basicStep(block: Long) {

    // Левая часть
    val leftPart = (block >>> BlockPartSize).toInt
    debugLeftPart(block, leftPart)

    // Правая часть
    val rightPart = (block << BlockPartSize >>> BlockPartSize).toInt
    debugRightPart(block, rightPart)

  }

  //---------------------DEBUG---------------------//

  /** Отладка левой части числа */
  private def debugLeftPart(block: Long, leftPart: Int) {
    val blockBin = block.toBinaryString
    // Правая часть числа
    val blockRight = blockBin.substring(BlockPartSize)
    val blockLeft = blockBin.substring(0, BlockPartSize)

    println(s"block: $blockLeft $blockRight")

    val leftPartBin = leftPart.toBinaryString
    println(s"lpart: $leftPartBin")

    assert(leftPartBin == blockLeft)
  }

  /** Отладка правой части числа */
  private def debugRightPart(block: Long, rightPart: Int) {
    val blockBin = block.toBinaryString
    // Правая часть числа
    val blockRight = blockBin.substring(BlockPartSize)
    // Правая часть без незначащих нолей
    val blocRightWithoutZeros = blockRight.substring(blockRight.indexOf("1"))

    println(s"block: ${blockBin.substring(0, BlockPartSize)} $blocRightWithoutZeros")

    val rightPartBin = rightPart.toBinaryString
    println(f"rpart: ${0 formatted s"%0${BlockPartSize}d"} $rightPartBin")

    assert(rightPartBin == blockRight)
  }

}
