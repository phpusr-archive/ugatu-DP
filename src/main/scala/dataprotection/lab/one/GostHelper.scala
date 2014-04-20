package dataprotection.lab.one

import scala.util.Random

/**
 * @author phpusr
 *         Date: 20.04.14
 *         Time: 16:13
 */

/**
 * Объект с функциями помогающими шифрованию ГОСТ-28147-89
 */
object GostHelper {

  /** Кол-во битов для сдвига */
  val ShiftBits = 32
  
  /** Генерирует 32-битное число */
  def generate32BitNumber = () => Random.nextInt().abs * -1

  /** Генерирует 64-битное число */
  def generate64BitNumber = () => Random.nextLong().abs * -1

  /** Возвращает левую часть 64-битного числа */
  def getLeftPart64BitNumber = (block: Long) => (block >>> ShiftBits).toInt

  /** Возвращает правую часть 64-битного числа */
  def getRightPart64BitNumber = (block: Long) => (block << ShiftBits >>> ShiftBits).toInt

}
