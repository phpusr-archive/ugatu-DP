package dataprotection.lab.three.types

import scala.util.Random
import dataprotection.lab.three.types.RsaType.RsaNumber

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 21:38
 */

object RsaType {

  type RsaNumber = Long

  def stringToRsaType(string: String): RsaNumber = string.toLong

  def getRandom(maxNumber: RsaNumber): RsaNumber = Random.nextLong().abs % maxNumber

  val Zero: RsaNumber = 0
  val One: RsaNumber = 1
  val Two: RsaNumber = 2


}

trait RsaTrait {

  /** Неявное добавление метода к строке */
  implicit class ExtendString(string: String) {
    /** String -> RsaNumber */
    def toRsaNumber: RsaNumber =  RsaType.stringToRsaType(string)
  }

}

