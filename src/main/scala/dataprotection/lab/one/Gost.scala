package dataprotection.lab.one

import scala.util.Random
import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer

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


  /** Размер блока шифрования в битах */
  private val BlockPartSize = 32

  /** Размер элементов блока S в битах */
  private val SBlockBitSize = 4

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

    (keySeq, keyHex)
  }

  /** Преобразование введенного ключа из 16-й строки в 10-й массив */
  def keyHexToKeyArray = (keyHex: String) => {
    keyHex.split(KeySplitter).map(java.lang.Long.parseLong(_, KeyOutputNotation).toInt)
  }

  /** Возвращает левую часть 64-битного числа */
  def getLeftPart64BitNumber = (block: Long) => (block >>> BlockPartSize).toInt

  /** Возвращает правую часть 64-битного числа */
  def getRightPart64BitNumber = (block: Long) => (block << BlockPartSize >>> BlockPartSize).toInt

}

class Gost(keyHexString: String) {

  import Gost.{BlockPartSize, SBlockBitSize}

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Ключ */
  private val key = Gost.keyHexToKeyArray(keyHexString)

  /** Число для нахождения остатка от деления */
  private val NumberForMod = Math.pow(2, BlockPartSize).toInt

  //---------------------------------------------//

  /** Основной шаг криптопреобразования */
  def basicStep(block: Long) { //TODO private

    // Левая часть
    val leftPart = Gost.getLeftPart64BitNumber(block)
    debugLeftPart(block, leftPart)

    // Правая часть
    val rightPart = Gost.getRightPart64BitNumber(block)
    debugRightPart(block, rightPart)

    val partKey = key(0) //TODO

    // Остаток от деления
    val sMod = (rightPart + partKey) % NumberForMod
    debugSMod(rightPart, partKey, sMod)

    // Остаток от деления делим на 8 блоков (по 4 бита)
    val shiftBitsCount = BlockPartSize - SBlockBitSize
    val sBlocskList = ListBuffer[Byte]()
    val SBlockCount = 8
    var sModShift = sMod
    for (i <- 1 to SBlockCount) {
      // Выделение последних 4-х бит
      val sBlock = (sModShift << shiftBitsCount >>> shiftBitsCount).toByte
      sBlocskList += sBlock
      // Отрезание последних 4-х бит, чтобы подойти к следующему блоку
      sModShift = sModShift >>> SBlockBitSize
    }
    debugSBlocks(sBlocskList, sMod)

  }

  //---------------------DEBUG---------------------//

  /** Отладка получения S-блоков */
  private def debugSBlocks(sBlocksList: Seq[Byte], sMod: Int) {
    val reverseSBlocksList = sBlocksList.reverse.to[ListBuffer]

    // Удаляем элементы в начале, если они == 0
    for (i <- 1 to reverseSBlocksList.size) {
      if (reverseSBlocksList(0) == 0) reverseSBlocksList.remove(0)
    }

    // Вывод найденных S-блоков (без начальных нулевых)
    val sBlocksListOutput = reverseSBlocksList.map(_.toInt.toBinaryString).mkString(" ")
    logger.debug(s"orig(${sBlocksList.size}): " + sBlocksListOutput)

    // Вывод S-блоков, полученных разбиением строки
    val testSBlocksStrList = sMod.toBinaryString.reverse.split(s"(?<=\\G.{4})").map(_.reverse).reverse
    logger.debug(s"test(${testSBlocksStrList.size}): " + testSBlocksStrList.mkString(" "))

    // Преобразование бинарных тестовых блоков в Byte
    val testSBlocksList = testSBlocksStrList.map(java.lang.Byte.parseByte(_, 2))

    // Проверка правильного нахождения S-блоков
    for (i <- 0 until reverseSBlocksList.size) {
      val sBlock = reverseSBlocksList(i)
      val testSBlock = testSBlocksList(i)

      println(s"$testSBlock == $sBlock")
      assert(sBlock == testSBlock)
    }

  }

  /** Отладка сложение по модулю 2 в 32 */
  private def debugSMod(rightPart: Int, partKey: Int, sMod: Int) {
    logger.title("Debug sMod")

    logger.debug("partKey: " + partKey.toBinaryString)

    val sum64: Long = rightPart.toLong + partKey.toLong
    logger.debug("sum64: " + sum64.toBinaryString)
    val sum32 = Gost.getRightPart64BitNumber(sum64)
    logger.debug("sum32: " + sum32.toBinaryString)

    val sModTest32 = sum32 % NumberForMod
    logger.debug("sModT: " + sModTest32.toBinaryString)

    // Проверка равенства правой части суммы и остатка от деления на (2^32)
    assert(sum32 == sModTest32)

    logger.debug("sModO: " + sMod.toBinaryString)

    // Проверка числа подсчитанного на Int и на Long
    assert(sMod == sModTest32)
  }

  /** Отладка левой части числа */
  private def debugLeftPart(block: Long, leftPart: Int) {
    logger.title("Debug left part")

    logger.debug("" + block)
    val blockBin = block.toBinaryString
    // Правая часть числа
    val blockRight = blockBin.substring(BlockPartSize)
    val blockLeft = blockBin.substring(0, BlockPartSize)

    logger.debug(s"block: $blockLeft $blockRight")

    val leftPartBin = leftPart.toBinaryString
    logger.debug(s"lpart: $leftPartBin")

    assert(leftPartBin == blockLeft)
  }

  /** Отладка правой части числа */
  private def debugRightPart(block: Long, rightPart: Int) {
    logger.title("Debug right part")

    logger.debug("" + block)
    val blockBin = block.toBinaryString
    // Правая часть числа
    val blockRight = blockBin.substring(BlockPartSize)
    // Правая часть без незначащих нолей
    val blocRightWithoutZeros = blockRight.substring(blockRight.indexOf("1"))

    logger.debug(s"block: ${blockBin.substring(0, BlockPartSize)} $blocRightWithoutZeros")

    val rightPartBin = rightPart.toBinaryString
    logger.debug(f"rpart: ${0 formatted s"%0${BlockPartSize}d"} $rightPartBin")

    assert(rightPartBin == blocRightWithoutZeros)
  }

}
