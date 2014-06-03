package dataprotection.rgr.idea

import dataprotection.rgr.idea.IDEA._
import org.dyndns.phpusr.bitnumber.BitNumber

/**
 * @author phpusr
 *         Date: 01.06.14
 *         Time: 20:58
 */

/**
 * Утилита для шифра IDEA
 */
trait IDEATools {

  /** 2 в 16 + 1 */
  private val Base = 0x10001

  /** Маска для выделения последних 16 бит из Int */
  private val Mask = 0xffff

  /** Умножение по модулю 2 в 16 + 1 */
  def mul(a: BitNumber, b: BitNumber) = {

    var x = a.toInt
    var y = b.toInt

    if (x == 0) x = Base - y
    else if (y == 0) x = Base - x
    else {
      val p = x * y
      y = p & Mask
      x = p >>> SubBlocksSize
      x = y - x + (if (y < x)  1 else 0)
    }

    val res = BitNumber(Array(x & Mask))
    res.last(SubBlocksSize)
  }

  /**
   * Функция инвертирует подключи шифрования в подключи расшифрования.
   * Она таже включает в себя функции для нахождения мультипликативного обратного и аддитивно обратного.
   */
  def invertKey(inKeySeq: Seq[Seq[BitNumber]]) = {
    /** Промежуточные значения */
    var t1, t2, t3, t4 = 0
    /** Входной ключ в виде Int */
    val inKey = inKeySeq.flatMap(_.toSeq).map(_.toInt)
    /** Инвертированный ключ */
    val key = new Array[Int](SubKeysCount)

    /** Индекс входного ключа */
    var inKeyIndex = 0
    /** Индекс выходного ключа */
    var outKeyIndex = SubKeysCount

    /** Возвращает подключ входного ключа и увеличивает индекс (inKeyIndex) */
    val getSubKey = () => {val value = inKey(inKeyIndex); inKeyIndex += 1; value}
    /** Уменьшает индекс (outKeyIndex) и устанавливает подключ выходного ключа */
    val setSubKey = (value: Int) => {outKeyIndex -= 1; key(outKeyIndex) = value}

    /** Набор операций 1 */
    val set1 = () => {
      t1 = getSubKey(); t2 = getSubKey()
      setSubKey(t2); setSubKey(t1)
    }

    /** Набор операций 2 */
    val set2 = () => {
      t1 = mulInv(getSubKey()); t2 = addInv(getSubKey())
      t3 = addInv(getSubKey()); t4 = mulInv(getSubKey())
      setSubKey(t4); setSubKey(t3)
      setSubKey(t2); setSubKey(t1)
    }

    set2()

    for (i <- 1 to 7) {
      set1()

      t1 = mulInv(getSubKey()); t2 = addInv(getSubKey())
      t3 = addInv(getSubKey()); t4 = mulInv(getSubKey())
      /* NB: Order */
      setSubKey(t4); setSubKey(t2)
      setSubKey(t3); setSubKey(t1)
    }

    set1()
    set2()

    key.map(e => BitNumber(Array(e)).last(KeyPartSize)).sliding(SubKeysBlocksCount, SubKeysBlocksCount).toList.map(_.toList)
  }

  /**
   * Вычисляет мультипликативное обратное используя алгоритм Евклида для нахождения НОД.
   * Ноли и один self inverse.
   * <p>
   * x * mulInv(x) == 1 (modulo BASE)
   */
  private def mulInv(xIn: Int): Int = {
    var t0 = 0
    var t1 = 0
    var q = 0
    var y = 0
    var x = xIn

    if (x < 2) return x

    t0 = 1
    t1 = Base / x
    y = Base % x
    while (y != 1) {
      q = x / y
      x = x % y
      t0 = (t0 + (t1 * q)) & Mask
      if (x == 1) {
        return t0
      }
      q = y / x
      y = y % x
      t1 = (t1 + (t0 * q)) & Mask
    }

    (1 - t1) & Mask
  }

  /**
   * Возвращает аддитивное обратное для x.
   * <p>
   * x + addInv(x) == 0
   */
  private def addInv(x: Int) =  (0 - x) & Mask

}
