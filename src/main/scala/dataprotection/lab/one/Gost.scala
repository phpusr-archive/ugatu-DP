package dataprotection.lab.one

import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer
import dataprotection.lab.one.GostHelper._
import dataprotection.lab.one.GostConstants._

/**
 * @author phpusr
 *         Date: 18.04.14
 *         Time: 15:25
 */

/**
 * TODO
 */
object Gost extends GostDebug {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  //---------------------------------------------//

  /** Генерация 256-битного ключ */
  def generateKey = () => {
    val KeyBlocksCount = 8

    // Список из 8-ми 32-х битных частей ключа
    val keySeq = for (i <- 1 to KeyBlocksCount) yield generate32BitNumber()

    val keyHex = keySeq.map(e => Integer.toHexString(e)).mkString(KeySplitter)

    (keySeq, keyHex)
  }

  /** Преобразование введенного ключа из 16-й строки в 10-й массив */
  private def keyHexToKeyArray = (keyHex: String) => {
    keyHex.split(KeySplitter).map(java.lang.Long.parseLong(_, KeyOutputNotation).toInt)
  }

  //---------------------------------------------//

  /** Шифрование блока */
  def encryptBlock(block: Long, key: String) = {
    val keyArray = keyHexToKeyArray(key)
    //TODO проверка на битность

    // Левая часть
    val leftPart = getLeftPart64BitNumber(block)
    debugLeftPart(block, leftPart)

    // Правая часть
    val rightPart = getRightPart64BitNumber(block)
    debugRightPart(block, rightPart)

    // Оболочка частей блока для шифрования
    var enc = Block(leftPart, rightPart)

    /** Функция запускающая базовый шаг криптопреобразования */
    def runBasicStep = (keyPart: Int) => enc = basicStep(enc.leftPart, enc.rightPart, keyPart)

    keyArray.foreach(runBasicStep)
    keyArray.foreach(runBasicStep)
    keyArray.foreach(runBasicStep)
    keyArray.reverse.foreach(runBasicStep)

    // Меняет местами левую и правую части
    enc = enc.swap
    debugEncryptBlock(enc)

    // Возврат соединенных блоков
    enc.allPart
  }

  /** Расшифрование блока */
  def decryptBlock(block: Long, key: String) = {
    val keyArray = keyHexToKeyArray(key)
    //TODO проверка на битность

    // Левая часть
    val leftPart = getLeftPart64BitNumber(block)
    debugLeftPart(block, leftPart)

    // Правая часть
    val rightPart = getRightPart64BitNumber(block)
    debugRightPart(block, rightPart)

    // Оболочка частей блока для расшифрования
    var dec = Block(leftPart, rightPart)

    /** Функция запускающая базовый шаг криптопреобразования */
    def runBasicStep = (keyPart: Int) => dec = basicStep(dec.leftPart, dec.rightPart, keyPart)

    keyArray.foreach(runBasicStep)
    keyArray.reverse.foreach(runBasicStep)
    keyArray.reverse.foreach(runBasicStep)
    keyArray.reverse.foreach(runBasicStep)

    // Меняет местами левую и правую части
    dec = dec.swap
    debugEncryptBlock(dec)

    // Возврат соединенных блоков
    dec.allPart
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

}
