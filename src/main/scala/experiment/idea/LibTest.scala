package experiment.idea

import org.dyndns.phpusr.bitnumber.BitNumber
import org.bouncycastle.crypto.params.KeyParameter

/**
 * @author phpusr
 *         Date: 01.06.14
 *         Time: 13:10
 */

/**
 * Проверка шифра на библиотечном
 */
object LibTest extends App {

  val keyArray = Array(0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8).map(_.toByte)
  val keyParam = new KeyParameter(keyArray)

  val engine = new IDEAEngine
  engine.init(true, keyParam)

  val dataArray = Array(0, 0, 0, 1, 0, 2, 0, 3).map(_.toByte)
  val out = new Array[Byte](dataArray.size)
  engine.processBlock(dataArray, 0, out, 0)

  val outBits = BitNumber(out)

  println(outBits.toHexStr.sliding(4, 4).toList)
}
