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

  /** Возвращает размер числа в битах */
  def size = bitList.size

  /** Конвертирует число в двоичную стркоу */
  def toBinStr = bitList.map(bitNumToChar).mkString

  override def toString = {
    s"$toBinStr($size)"
  }



}
