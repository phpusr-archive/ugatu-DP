package org.dyndns.phpusr.bitnumber

import org.dyndns.phpusr.util.log.Logger
import scala.collection.mutable.ListBuffer
import org.dyndns.phpusr.bitnumber.BitNum._

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
    val bitList = for (i <- 0 until size) yield Zero
    new BitNumber(bitList)
  }

  /** Создание BitNumber из Int */
  private def createBitNumberFromInt(value: Int) = {
    val valueBin = (value.toBinaryString formatted s"%${IntSize}s").replace(' ', ZeroChar)
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

  /** Логгер */
  private val logger = new Logger(true, true, true)

  //--------------------------------------------------------------//

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

  /** Возвращает последние биты */
  def last(n: Int) = {
    val list = _number.slice(size-n, size)
    new BitNumber(list)
  }

  /** Разбиение на части */
  def split(partSize: Int) = {
    _number.sliding(partSize, partSize).map(new BitNumber(_)).toList
  }

  /** Сдвиг влево */
  def <(shiftSize: Int) = {
    val list = ListBuffer[BitNum]()
    list ++= _number
    for (i <- 0 until shiftSize) list += BitNum.Zero

    new BitNumber(list)
  }

  /** Побитовое умножение */
  def *(otherNumber: BitNumber) = {
    if (size != otherNumber.size) throw new IllegalArgumentException("Size do not match")

    val res = _number.zip(otherNumber.bits).map { case (x, y) => x * y }
    new BitNumber(res)
  }

  /** Умножение на бит */
  def *(bit: BitNum) = new BitNumber(_number.map(_ * bit))

  /** XOR */
  def xor(otherNumber: BitNumber) = {
    if (size != otherNumber.size) throw new IllegalArgumentException("Size do not match")

    val res = _number.zip(otherNumber.bits).map { case (x, y) => x xor y }
    new BitNumber(res)
  }

  /** Сложение */
  def +(otherNumber: BitNumber) = {
    val maxSize = Math.max(size, otherNumber.size)

    val notSupportCaseException = new Exception("Not support case exception")

    val list = ListBuffer[BitNum]()
    var memory = Zero
    for (i <- 1 to maxSize) {
      // Если у какого-то из числе меньше разрядов, то они заменяются нулями
      val x = if (i <= size) _number(size-i) else BitNum.Zero
      val y = if (i <= otherNumber.size) otherNumber.bits(otherNumber.size-i) else BitNum.Zero

      if (false) print(s"$x + $y ($memory) = ")

      val bit = if (memory == Zero) { // Если есть перенос бита
        if (x == Zero && y == One || x == One && y == Zero) One
        else if (x == Zero && y == Zero) Zero
        else if (x == One && y == One) {
          memory = One
          Zero
        } else throw notSupportCaseException
      } else if (memory == One) { // Если нет переноса бита
        if (x == Zero && y == One || x == One && y == Zero) Zero
        else if (x == Zero && y == Zero) {
          memory = Zero
          One
        } else if (x == One && y == One) One
        else throw notSupportCaseException
      } else throw notSupportCaseException

      if (false) println(""+bit)

      list += bit
    }

    // Добавление еще одно разряда в начале если нужно
    if (memory == One) list += memory

    new BitNumber(list.reverse)
  }

  /** Добавление */
  def +=(otherNumber: BitNumber) = {
    val res = this + otherNumber
    _number.clear()
    _number ++= res.bits
  }

  /** Умножение */
  def **(otherNumber: BitNumber) = {
    var sum = BitNumber(0)
    if (false) println(otherNumber.toString)
    otherNumber.bits.reverse.zipWithIndex.foreach { case (bit, index) =>
      val res = (this * bit) < index
      if (false) println(s"$index: $res")
      sum += res
    }

    sum
  }

  /** Клонирование */
  override def clone = new BitNumber(_number.toList)

  override def toString = s"$toBinStr($size)"

}
