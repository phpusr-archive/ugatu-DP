package dataprotection.lab.one

/**
 * @author phpusr
 *         Date: 20.04.14
 *         Time: 13:03
 */

/**
 * Класс для хранения частей блока
 */
case class Block(leftPart: Int, rightPart: Int) {

  /** Размер частей блока */
  private val PartSize = Gost.BlockPartSize

  /** Получение из двух 32-битных частей одну 64-ю */
  def allPart = {
    (leftPart.toLong << PartSize) | rightPart
  }

}

