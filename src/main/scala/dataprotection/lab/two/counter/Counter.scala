package dataprotection.lab.two.counter

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 18:46
 */

/** Счетчик */
class Counter() {

  private var _value = 0

  def value = _value
  def value_= (newValue: Int) = _value = newValue

  def inc() = {_value += 1; _value}
  def reset() = {_value = 0; _value}
  override def toString = _value.toString
}
