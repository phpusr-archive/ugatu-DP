package dataprotection.lab.two.counter

import org.scalatest.FlatSpec

/**
 * @author phpusr
 *         Date: 25.04.14
 *         Time: 18:55
 */

/**
 * Тестирование класса Counter
 */
class CounterSpec extends FlatSpec {

  /** Тестирование работы счетчика */
  it should "normal work counter" in {
    val index = new Counter

    assert(index.value == 0)
    assert(index.inc() == 1)
    assert(index.inc() == 2)
    assert(index.reset() == 0)
  }

}
