package dataprotection.lab.two.bitnumber

import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer
import experiment.bitnumber.BitNum

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 10:49
 */

/**
 * Объект для создания BitNumber
 */
object BitNumber {

  /** Размер в битах для Int */
  private val IntSize = 32
  /** Размер в битах для Byte */
  private val ByteSize = 8

  /** Логгер */
  private val logger = new Logger(true, true, true)

  //--------------------------------------------------------------//

  /** Создание BitNumber на основе массива Int */
  def apply(value: Array[Int]): BitNumber = {
    val number = apply(0)
    value.foreach { e =>
      val bit = createBitNumberFromInt(e)
      number.join(bit)
    }

    number
  }

  /** Создание BitNumber на основе массива Byte */
  def apply(value: Array[Byte]): BitNumber = {
    val number = apply(0)
    value.foreach { e =>
      val bit = createBitNumberFromByte(e)
      number.join(bit)
    }

    number
  }

  /** Создание BitNumber = 0, на основе его длины */
  def apply(size: Int): BitNumber = {
    val bitList = for (i <- 0 until size) yield BitNum.Zero
    new BitNumber(bitList)
  }

  /** Создание BitNumber из Int */
  private def createBitNumberFromInt(value: Int) = {
    val valueBin = (value.toBinaryString formatted s"%${IntSize}s").replace(' ', BitNum.ZeroChar)
    val bitList = valueBin.map(BitNum(_))
    new BitNumber(bitList)
  }

  /** Создание BitNumber из Byte */
  private def createBitNumberFromByte(value: Byte) = {
    val bitList = createBitNumberFromInt(value).bits.slice(IntSize - ByteSize, IntSize)
    new BitNumber(bitList)
  }

}

/**
 * Класс для хранения числа состоящего различного количества бит
 */
class BitNumber(bitList: Seq[BitNum]) {

  /** Представление числа */
  private val _number = {
    val list = ListBuffer[BitNum]()
    list ++= bitList
    list
  }

  /** Массив битов числа */
  def bits = _number.toList

  /** Возвращает размер числа в битах */
  def size = _number.size

  /** Конвертирует число в двоичную стркоу */
  def toBinStr = _number.map(_.toChar).mkString

  /** Возвращает бит по указанному индексу */
  def apply(index: Int) = _number(index).toChar

  /** Устанавливает бит по указанному индексу */
  def set(index: Int, value: BitNum) = _number(index) = value

  /** Соединение с другим */
  def join(otherNumber: BitNumber) =  _number ++= otherNumber.bits

  /** Побитовое умножение */
  def *(otherNumber: BitNumber) = {
    if (size != otherNumber.size) throw new IllegalArgumentException("Size do not match")

    val res = _number.zip(otherNumber.bits).map { case (x, y) => x * y }
    new BitNumber(res)
  }


  override def toString = s"$toBinStr($size)"

}
