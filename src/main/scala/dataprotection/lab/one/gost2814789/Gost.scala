package dataprotection.lab.one.gost2814789

import scala.collection.mutable.ListBuffer
import org.dyndns.phpusr.util.log.Logger
import dataprotection.lab.one.block.Block
import dataprotection.lab.one.gost2814789.tools.GostDebug
import dataprotection.lab.one.gost2814789.tools.GostHelper._
import dataprotection.lab.one.gost2814789.GostConstants._

/**
 * @author phpusr
 *         Date: 18.04.14
 *         Time: 15:25
 */

/**
 * Алгоритм шифрования
 * ГОСТ-28147-89 <br/>
 * написан по статье: <u><a href="http://www.wasm.ru/wault/article/show/gost29147-89">ссылка</a></u>
 */
object Gost extends GostDebug {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  //---------------------------------------------//

  /** TODO */
  def encryptBlockArray(blockArray: Array[Long], keyArray: Array[Int]) = {
    blockArray.map(encryptBlock(_, keyArray))
  }

  /** TODO */
  def decryptBlockArray(blockArray: Array[Long], keyArray: Array[Int]) = {
    blockArray.map(decryptBlock(_, keyArray))
  }

  /** Шифрование блока */
  private def encryptBlock(block: Long, keyArray: Array[Int]) = encryptOrDecryptBlock(block, keyArray, encrypt = true)

  /** Расшифрование блока */
  private def decryptBlock(block: Long, keyArray: Array[Int]) = encryptOrDecryptBlock(block, keyArray, encrypt = false)

  /** Шифрование блока */
  private def encryptOrDecryptBlock(block: Long, keyArray: Array[Int], encrypt: Boolean) = {
    if (keyArray.size != KeyBlocksCount) throw new IllegalArgumentException("Wrong key size")

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

    // Выбор порядка подачи ключей в зависимости, что нжуно делать: шифровать или расшифровывать
    if (encrypt) {
      for (i <- 1 to 3) keyArray.foreach(runBasicStep)
      keyArray.reverse.foreach(runBasicStep)
    } else {
      keyArray.foreach(runBasicStep)
      for (i <- 1 to 3) keyArray.reverse.foreach(runBasicStep)
    }

    // Меняет местами левую и правую части
    enc = enc.swap
    debugEncryptBlock(enc)

    // Возврат соединенных блоков
    enc.allParts
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
