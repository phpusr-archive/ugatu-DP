package experiment.gost.wasm

import scala.runtime.RichInt

/**
 * @author phpusr
 *         Date: 18.04.14
 *         Time: 13:15
 */

/**
 * Примеры из статьи
 * http://www.wasm.ru/wault/article/show/gost29147-89
 */
object Example {

  val L = 14
  val R = 5
  val K = 15
  val list = List(L, R, K)
  list.map(toBin)
  val Smod = ((R + K) % Math.pow(2, 4)).toInt
  toBin(Smod)

  val Sxor = Smod + L
  toBin(Sxor)

  def toBin(num: AnyVal) = {
    num match {
      case _:Int => new RichInt(num.asInstanceOf[Int]).toBinaryString
    }
  }
}
