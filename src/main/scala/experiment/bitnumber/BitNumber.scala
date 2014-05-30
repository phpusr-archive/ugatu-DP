package dataprotection.lab.two.bitnumber

import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer

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
  def apply(value: Array[Int]): BitNumber = {
    val number = apply(0)
    value.foreach { e =>
      createBitNumberFromInt(e)
      number.join _
    }

    number
  }

  /** Создание BitNumber = 0, на основе его длины */
  def apply(size: Int): BitNumber = {
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
  private val _number = {
    val list = ListBuffer[Boolean]()
    list ++= bitList
    list
  }

  /** Массив битов числа */
  val bits = _number.toList

  /** Возвращает размер числа в битах */
  def size = _number.size

  /** Конвертирует число в двоичную стркоу */
  def toBinStr = _number.map(bitNumToChar).mkString

  /** Возвращает бит по указанному индексу */
  def apply(index: Int) = bitNumToChar(_number(index))

  /** Устанавливает бит по указанному индексу */
  def set(index: Int, value: BitNum) = _number(index) = value

  /** Соединение с другим */
  def join(otherNumber: BitNumber) =  _number ++= otherNumber.bits


  override def toString = s"$toBinStr($size)"

}
