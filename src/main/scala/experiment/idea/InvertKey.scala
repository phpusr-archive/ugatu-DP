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
    var t1, t2, t3, t4 = 0
    var p = SubKeysCount
    val key = new Array[Int](SubKeysCount)
    var inOff = 0

    val inKey = inKeySeq.flatMap(_.toSeq).map(_.toInt)

    val inOffInc = () => {val value = inKey(inOff); inOff += 1; value}
    val setKey = (value: Int) => {p -= 1; key(p) = value}

    t1 = mulInv(inOffInc())
    t2 = addInv(inOffInc())
    t3 = addInv(inOffInc())
    t4 = mulInv(inOffInc())
    setKey(t4)
    setKey(t3)
    setKey(t2)
    setKey(t1)

    for (i <- 1 to 7) {
      t1 = inOffInc()
      t2 = inOffInc()
      setKey(t2)
      setKey(t1)

      t1 = mulInv(inOffInc())
      t2 = addInv(inOffInc())
      t3 = addInv(inOffInc())
      t4 = mulInv(inOffInc())
      setKey(t4)
      setKey(t2) /* NB: Order */
      setKey(t3)
      setKey(t1)
    }

    t1 = inOffInc()
    t2 = inOffInc()
    setKey(t2)
    setKey(t1)

    t1 = mulInv(inOffInc())
    t2 = addInv(inOffInc())
    t3 = addInv(inOffInc())
    t4 = mulInv(inOffInc())
    setKey(t4)
    setKey(t3)
    setKey(t2)
    setKey(t1)

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
