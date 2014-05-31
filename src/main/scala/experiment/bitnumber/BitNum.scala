package experiment.bitnumber

/**
 * @author phpusr
 *         Date: 30.05.14
 *         Time: 17:58
 */

/**
 * Тип для одного бита числа
 */
case class BitNum(value: Boolean) {
  import experiment.bitnumber.BitNum._
  
  def toChar = if (value) OneChar else ZeroChar

  /** Операция умножения */
  def *(otherBit: BitNum) = BitNum(value && otherBit.value)

  /** Операция XOR */
  def xor(otherBit: BitNum) = BitNum(value ^ otherBit.value)

  override def toString = toChar.toString
}

object BitNum {
  val ZeroChar = '0'
  val Zero = new BitNum(false)
  val OneChar = '1'
  val One = new BitNum(true)
  
  def apply(char: Char) = {
    char match {
      case ZeroChar => new BitNum(false)
      case OneChar => new BitNum(true)
      case _ => throw new IllegalArgumentException("Not support Char: " + char)
    }
  }
}
