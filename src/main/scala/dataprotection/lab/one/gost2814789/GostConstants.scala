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

  /** Система счисления для вывода ключа */
  val KeyOutputNotation = 16

  /** Разделитель блоков при выводе ключа */
  val KeySplitter = " "

  /** Размер ключа в битах */
  //TODO задействовать при проверке ключа
  val KeySize = 256

  /** Кол-во блоков ключа */
  val KeyBlocksCount = 8

  /** Размер блока шифрования в битах */
  val BlockSize = 64

  /** Размер блока шифрования в битах */
  val BlockPartSize = 32

  /** Число для нахождения остатка от деления */
  val NumberForMod = Math.pow(2, BlockPartSize).toInt

  /** Размер элементов блока S в битах */
  val SBlockBitSize = 4

  /** Таблица замен */
  val ReplaceTbl = ReplaceTable.default

  /** Кодировка сообщения для шифрования */
  val CharsetName = "utf8"

}
