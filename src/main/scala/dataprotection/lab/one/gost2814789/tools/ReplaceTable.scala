package dataprotection.lab.one.gost2814789.tools

/**
 * @author phpusr
 *         Date: 14.04.14
 *         Time: 16:54
 */

/**
 * Таблица замен
 */
object ReplaceTable {

  /**
   * Стандартная таблица замен <br/>
   * Данный набор S-блоков используется в криптографических приложениях ЦБ РФ
   * <a href="http://ru.wikipedia.org/wiki/%D0%93%D0%9E%D0%A1%D0%A2_28147-89">link</a>
   */
  val default = Array[Array[Byte]](
    Array[Byte](0x4,0xA,0x9,0x2,0xD,0x8,0x0,0xE,0x6,0xB,0x1,0xC,0x7,0xF,0x5,0x3),
    Array[Byte](0xE,0xB,0x4,0xC,0x6,0xD,0xF,0xA,0x2,0x3,0x8,0x1,0x0,0x7,0x5,0x9),
    Array[Byte](0x5,0x8,0x1,0xD,0xA,0x3,0x4,0x2,0xE,0xF,0xC,0x7,0x6,0x0,0x9,0xB),
    Array[Byte](0x7,0xD,0xA,0x1,0x0,0x8,0x9,0xF,0xE,0x4,0x6,0xC,0xB,0x2,0x5,0x3),
    Array[Byte](0x6,0xC,0x7,0x1,0x5,0xF,0xD,0x8,0x4,0xA,0x9,0xE,0x0,0x3,0xB,0x2),
    Array[Byte](0x4,0xB,0xA,0x0,0x7,0x2,0x1,0xD,0x3,0x6,0x8,0x5,0x9,0xC,0xF,0xE),
    Array[Byte](0xD,0xB,0x4,0x1,0x3,0xF,0x5,0x9,0x0,0xA,0xE,0x7,0x6,0x8,0x2,0xC),
    Array[Byte](0x1,0xF,0xD,0x0,0x5,0x7,0xA,0x4,0x9,0x2,0x3,0xE,0x6,0xB,0x8,0xC)
  )
}