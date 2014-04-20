package dataprotection.lab.one

import scala.collection.mutable.ListBuffer
import org.dyndns.phpusr.util.log.Logger
import dataprotection.lab.one.GostHelper._
import dataprotection.lab.one.GostConstants._

/**
 * @author phpusr
 *         Date: 20.04.14
 *         Time: 15:55
 */

/**
 * Отладка ГОСТ-28147-89
 * //TODO отключение дебага
 */
trait GostDebug {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  /** Проверка шифрования блока */
  protected def debugEncryptBlock(enc: Block) {
    logger.title("Debug encrypt block")

    logger.debug("lPart: " + enc.leftPart.toBinaryString + s" (${enc.leftPart}})")
    logger.debug("rPart: " + enc.rightPart.toBinaryString + s" (${enc.rightPart}})")
    val resutl = enc.allPart
    logger.debug("all  : " + resutl.toBinaryString)

    // Откусывание левой части и сравнение ее с оригиналом
    val testLeftPart = getLeftPart64BitNumber(resutl)
    logger.debug(s"${testLeftPart.toBinaryString} == ${enc.leftPart.toBinaryString}")
    assert(testLeftPart == enc.leftPart)

    // Откусывание правой части и сравнение ее с оригиналом
    val testRightPart = getRightPart64BitNumber(resutl)
    logger.debug(s"${testRightPart.toBinaryString} == ${enc.rightPart.toBinaryString}")
    assert(testRightPart == enc.rightPart)
  }

  /** Проверка обработки левой части */
  protected def debugSXor(leftPart: Int, sRol: Int, sXor: Int) {
    logger.title("Debug processing leftPart")

    logger.debug("sRol: " + sRol.toBinaryString)
    val testXor = sXor ^ sRol

    assert(testXor == leftPart)
  }

  /** Проверка сдвига sMod на 11 бит */
  protected def debugSSimpleShiftBits(sSimple: Int, sRol: Int, shiftBits: Int) {
    logger.title("Debug sSimple shift 11 bits")

    val testSRol = Integer.rotateLeft(sSimple, shiftBits)
    logger.debug(s"${testSRol.toBinaryString} == ${sRol.toBinaryString}")

    assert(testSRol == sRol)
  }

  /** Отладка соединения S-блоков */
  protected def debugJoinSBlocks(sSimpleBlocks: Seq[Byte], sSimple: Int) {
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
  protected def debugSBlocks(sBlocksList: Seq[Byte], sMod: Int) {
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
  protected def debugSMod(rightPart: Int, partKey: Int, sMod: Int) {
    logger.title("Debug sMod")

    logger.debug("keyPart: " + partKey.toBinaryString)

    val sum64: Long = rightPart.toLong + partKey.toLong
    logger.debug("sum64: " + sum64.toBinaryString)
    val sum32 = getRightPart64BitNumber(sum64)
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
  protected def debugLeftPart(block: Long, leftPart: Int) {
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
  protected def debugRightPart(block: Long, rightPart: Int) {
    logger.title("Debug right part")

    logger.debug("" + block)
    val blockBin = block.toBinaryString
    // Правая часть числа
    val splitIndex = blockBin.size - BlockPartSize
    val blockRight = blockBin.substring(splitIndex)
    // Правая часть без незначащих нолей
    val blocRightWithoutZeros = blockRight.substring(blockRight.indexOf("1"))

    logger.debug(s"block: ${blockBin.substring(0, BlockPartSize)} $blocRightWithoutZeros")

    val rightPartBin = rightPart.toBinaryString
    logger.debug(f"rpart: ${0 formatted s"%0${BlockPartSize}d"} $rightPartBin")

    assert(rightPartBin == blocRightWithoutZeros)
  }

}
