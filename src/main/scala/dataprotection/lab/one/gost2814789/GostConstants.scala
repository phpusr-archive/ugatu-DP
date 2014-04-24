package dataprotection.lab.one.gost2814789

import dataprotection.lab.one.gost2814789.tools.ReplaceTable

/**
 * @author phpusr
 *         Date: 20.04.14
 *         Time: 16:29
 */

/**
 * Константы для ГОСТ-28147-89
 */
object GostConstants {

  /** Кол-во блоков ключа */
  val KeyBlocksCount = 8

  /** Размер блока шифрования в битах */
  val BlockSize = 64

  /** Размер блока шифрования в битах */
  val BlockPartSize = 32

  /** Размер байта в битах */
  val ByteSize = 8

  /** Кол-во байтов в 64 битах */
  val ByteInLongCount = 64 / 8

  /** Заполнитель оставшегося места в блоке */
  val Aggregate = '_'.toByte

  /** Число для нахождения остатка от деления */
  val NumberForMod = Math.pow(2, BlockPartSize).toInt

  /** Размер элементов блока S в битах */
  val SBlockBitSize = 4

  /** Таблица замен */
  val ReplaceTbl = ReplaceTable.default

  /** Кодировка сообщения для шифрования */
  val CharsetName = "cp1251"

}
