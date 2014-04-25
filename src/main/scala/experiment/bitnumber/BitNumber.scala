package dataprotection.lab.two.bitnumber

import org.dyndns.phpusr.util.log.Logger

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 10:49
 */

/**
 * Объект для создания BitNumber
 */
object BitNumber {
  
  /** Тип для одного бита числа */
  private type BitNum = Boolean

  /** Размер в битах для Int */
  private val IntSize = 32

  // Константы для конвертирования
  private val Zero = false
  private val ZeroChar = '0'
  private val One = true
  private val OneChar = '1'

  /** Логгер */
  private val logger = new Logger(true, true, true)

  //--------------------------------------------------------------//

  /** Создание BitNumber на основе какого-то значения */
  def apply(value :AnyVal) = {
    value match {
      case _:Int => createBitNumberFromInt(value.asInstanceOf[Int])
      case _ => throw new IllegalArgumentException("Not support type")
    }
  }

  /** Создание BitNumber = 0, на основе его длины */
  def apply(size: Int, init: Boolean) = {
    val bitList = for (i <- 0 until size) yield Zero
    new BitNumber(bitList)
  }

  /** Создание BitNumber из Int */
  private def createBitNumberFromInt(value: Int) = {
    val valueBin = (value.toBinaryString formatted s"%${IntSize}s").replace(' ', ZeroChar)
    val bitList = valueBin.map(charToBitNum)
    new BitNumber(bitList)
  }

  /** Char -> BitNum */
  private def charToBitNum(char: Char) = {
    char match {
      case ZeroChar => Zero
      case OneChar => One
      case _ => throw new IllegalArgumentException("Not support Char: " + char)
    }
  }

  /** BitNum -> Char */
  private def bitNumToChar(bitNum: BitNum) = {
    bitNum match {
      case Zero => ZeroChar
      case One => OneChar
      case _ => throw new IllegalArgumentException("Not support BitNum: " + bitNum)
    }
  }

}

/**
 * Класс для хранения числа состоящего различного количества бит
 */
class BitNumber(bitList: Seq[Boolean]) {
  import dataprotection.lab.two.bitnumber.BitNumber._

  /** Представление числа */
  private val number = bitList.toArray

  /** Возвращает размер числа в битах */
  def size = number.size

  /** Конвертирует число в двоичную стркоу */
  def toBinStr = number.map(bitNumToChar).mkString

  /** Возвращает бит по указанному индексу */
  def apply(index: Int) = bitNumToChar(number(index))

  /** Устанавливает бит по указанному индексу */
  def set(index: Int, value: BitNum) = number(index) = value


  override def toString = s"$toBinStr($size)"

}
