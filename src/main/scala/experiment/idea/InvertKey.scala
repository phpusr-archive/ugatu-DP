package experiment.idea

import experiment.idea.IDEA._
import org.dyndns.phpusr.bitnumber.BitNumber

/**
 * @author phpusr
 *         Date: 01.06.14
 *         Time: 20:58
 */

/**
 * Инверсия ключа
 */
object InvertKey {

  /**
   * The function to invert the encryption subkey to the decryption subkey.
   * It also involves the multiplicative inverse and the additive inverse functions.
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
   * This function computes multiplicative inverse using Euclid's Greatest
   * Common Divisor algorithm. Zero and one are self inverse.
   * <p>
   * i.e. x * mulInv(x) == 1 (modulo BASE)
   */
  def mulInv(xIn: Int): Int = {
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
   * Return the additive inverse of x.
   * <p>
   * i.e. x + addInv(x) == 0
   */
  def addInv(x: Int) =  (0 - x) & Mask

}
