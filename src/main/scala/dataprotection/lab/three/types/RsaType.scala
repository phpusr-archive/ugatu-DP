package dataprotection.lab.three.types

import scala.util.Random

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 21:38
 */

object RsaType {

  type RsaNumber = Int

  def stringToRsaType(string: String): RsaNumber = string.toInt

  def getRandom(maxNumber: RsaNumber): RsaNumber = Random.nextInt() % maxNumber

  val One: RsaNumber = 1
  val Two: RsaNumber = 2

}

