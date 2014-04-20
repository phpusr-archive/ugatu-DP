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
  val BlockPartSize = 32

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

  import Gost.BlockPartSize

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Ключ */ //TODO проверка на битность
  private val key = Gost.keyHexToKeyArray(keyHexString)

  /** Число для нахождения остатка от деления */
  private val NumberForMod = Math.pow(2, BlockPartSize).toInt

  /** Размер элементов блока S в битах */
  private val SBlockBitSize = 4

  /** Таблица замен */
  private val ReplaceTbl = ReplaceTable.default

  //---------------------------------------------//

  /** Шифрование блока */
  def encryptBlock(block: Long) = {
    //TODO проверка на битность

    // Левая часть
    val leftPart = Gost.getLeftPart64BitNumber(block)
    debugLeftPart(block, leftPart)

    // Правая часть
    val rightPart = Gost.getRightPart64BitNumber(block)
    debugRightPart(block, rightPart)

    // Оболочка для частей блока для шифрования
    var enc = Block(leftPart, rightPart)

    // Функция запускающая базовый шаг криптопреобразования
    def runBasicStep = (keyPart: Int) => enc = basicStep(enc.leftPart, enc.rightPart, keyPart)

    key.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))
    key.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))
    key.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))
    key.reverse.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))

    enc = Block(enc.rightPart, enc.leftPart)
    debugEncryptBlock(enc)

    // Возврат соединенных блоков
    enc.allPart
  }

  /** Расшифрование блока */
  def decryptBlock(block: Long) = {

    // Левая часть
    val leftPart = Gost.getLeftPart64BitNumber(block)
    debugLeftPart(block, leftPart)

    // Правая часть
    val rightPart = Gost.getRightPart64BitNumber(block)
    debugRightPart(block, rightPart)

    // Оболочка для частей блока для шифрования
    var enc: Block = Block(leftPart, rightPart)

    // Функция запускающая базовый шаг криптопреобразования
    def runBasicStep = (keyPart: Int) => enc = basicStep(enc.leftPart, enc.rightPart, keyPart)

    key.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))
    key.reverse.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))
    key.reverse.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))
    key.reverse.foreach(keyPart => enc = basicStep(enc.leftPart, enc.rightPart, keyPart))

    enc = Block(enc.rightPart, enc.leftPart)
    debugEncryptBlock(enc)

    // Возврат соединенных блоков
    enc.allPart
  }

  /** Основной шаг криптопреобразования */
  private def basicStep(leftPart: Int, rightPart: Int, keyPart: Int) = {

    // Остаток от деления
    val sMod = (rightPart + keyPart) % NumberForMod
    debugSMod(rightPart, keyPart, sMod)

    // Остаток от деления, делим на 8 блоков (по 4 бита)
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

    // Замена блоков
    val sSimpleBlocks = for (i <- 0 until sBlocskList.size) yield ReplaceTbl(i)(sBlocskList(i))
    // Преобразование блоков в одно число
    var sSimple = 0
    for (i <- 0 until sSimpleBlocks.size) {
      val shiftBits = BlockPartSize - SBlockBitSize * (i+1)
      sSimple += sSimpleBlocks(i) << shiftBits
    }
    debugJoinSBlocks(sSimpleBlocks, sSimple)

    // Циклический сдвиг влево на 11 бит
    val ShiftBits = 11
    val sRol = (sSimple << ShiftBits) | (sSimple >>> (BlockPartSize - ShiftBits))
    debugSSimpleShiftBits(sSimple, sRol, ShiftBits)

    // Обработка левой части блока
    val sXor = leftPart ^ sRol
    debugSXor(leftPart, sRol, sXor)

    Block(rightPart, sXor)
  }

  //---------------------DEBUG---------------------//

  /** Проверка шифрования блока */
  private def debugEncryptBlock(enc: Block) {
    logger.title("Debug encrypt block")

    logger.debug("lPart: " + enc.leftPart.toBinaryString + s" (${enc.leftPart}})")
    logger.debug("rPart: " + enc.rightPart.toBinaryString + s" (${enc.rightPart}})")
    val resutl = enc.allPart
    logger.debug("all  : " + resutl.toBinaryString)

    // Откусывание левой части и сравнение ее с оригиналом
    val testLeftPart = Gost.getLeftPart64BitNumber(resutl)
    logger.debug(s"${testLeftPart.toBinaryString} == ${enc.leftPart.toBinaryString}")
    assert(testLeftPart == enc.leftPart)

    // Откусывание правой части и сравнение ее с оригиналом
    val testRightPart = Gost.getRightPart64BitNumber(resutl)
    logger.debug(s"${testRightPart.toBinaryString} == ${enc.rightPart.toBinaryString}")
    assert(testRightPart == enc.rightPart)
  }

  /** Проверка обработки левой части */
  private def debugSXor(leftPart: Int, sRol: Int, sXor: Int) {
    logger.title("Debug processing leftPart")

    logger.debug("sRol: " + sRol.toBinaryString)
    val testXor = sXor ^ sRol

    assert(testXor == leftPart)
  }

  /** Проверка сдвига sMod на 11 бит */
  private def debugSSimpleShiftBits(sSimple: Int, sRol: Int, shiftBits: Int) {
    logger.title("Debug sSimple shift 11 bits")

    val testSRol = Integer.rotateLeft(sSimple, shiftBits)
    logger.debug(s"${testSRol.toBinaryString} == ${sRol.toBinaryString}")

    assert(testSRol == sRol)
  }

  /** Отладка соединения S-блоков */
  private def debugJoinSBlocks(sSimpleBlocks: Seq[Byte], sSimple: Int) {
    logger.title("Debug join S-blocks")

    // Изменения списка в изменяемый
    val sSimpleBuffer = sSimpleBlocks.to[ListBuffer]

    // Удаляем элементы в начале, если они == 0
    for (i <- 1 to sSimpleBuffer.size) {
      if (sSimpleBuffer(0) == 0) sSimpleBuffer.remove(0)
    }

    logger.debug("sSimple: " + sSimpleBuffer.map(_.toInt.toBinaryString).mkString(" "))

    val testBlocksBin = sSimple.toBinaryString.reverse.split(s"(?<=\\G.{4})").map(_.reverse).reverse
    logger.debug("test   : " + testBlocksBin.mkString(" "))

    // Преобразование бинарных тестовых блоков в Byte
    val testBlocks = testBlocksBin.map(java.lang.Byte.parseByte(_, 2))

    // Проверка правильности соединения блоков
    for (i <- 0 until sSimpleBuffer.size) {
      val sBlock = sSimpleBuffer(i)
      val testSBlock = testBlocks(i)

      logger.trace(s"$testSBlock == $sBlock")
      assert(sBlock == testSBlock)
    }

    logger.debug("sSimple: " + sSimple.toBinaryString)

  }

  /** Отладка получения S-блоков */
  private def debugSBlocks(sBlocksList: Seq[Byte], sMod: Int) {
    logger.title("Debug sBlocks")

    // Изменения списка в изменяемый
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

      logger.trace(s"$testSBlock == $sBlock")
      assert(sBlock == testSBlock)
    }

  }

  /** Отладка сложение по модулю 2 в 32 */
  private def debugSMod(rightPart: Int, partKey: Int, sMod: Int) {
    logger.title("Debug sMod")

    logger.debug("keyPart: " + partKey.toBinaryString)

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
    val splitIndex = blockBin.size - BlockPartSize
    val blockRight = blockBin.substring(splitIndex)
    val blockLeft = blockBin.substring(0, splitIndex)

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
