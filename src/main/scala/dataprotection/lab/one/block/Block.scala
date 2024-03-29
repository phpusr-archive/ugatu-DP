package dataprotection.lab.one.block

import dataprotection.lab.one.gost2814789.GostConstants

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
  private val PartSize = GostConstants.BlockPartSize

  /** Получение из двух 32-битных частей одну 64-ю */
  def allParts = {
    // Получение 64-битного числа с первой половиной нолей
    val rightPart64 = rightPart.toLong << PartSize >>> PartSize
    // Сдвиг левой части в первую половину и сложение со второй
    (leftPart.toLong << PartSize) | rightPart64
  }

  /** Меняет местами левую и правую части */
  def swap = Block(rightPart, leftPart)

}

