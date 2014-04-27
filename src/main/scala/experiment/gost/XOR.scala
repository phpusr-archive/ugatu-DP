package experiment.gost

import java.nio.charset.StandardCharsets

/**
 * @author phpusr
 *         Date: 14.04.14
 *         Time: 12:21
 */

/**
 * Шифрование текста с помощью XOR
 * Используется симметричный ключ
 */
object XOR extends App {

  val CharsetName = StandardCharsets.UTF_16BE

  val text = "я ."
  val key = "га"

  var1()
  println("\n------\n")
  var2()

  /**
   * Работет только с: cp1251, UTF_16BE, UTF_16LE
   * Принимает строку и возвращает строку
   * Неправильная конвертация происходит, когда закодированный массив байтов переводится в строку
   * Видимо в кодировке не хватает каких-то символов пэ некоторые числа не правильно переводятся в символы
   * В последующей конвертации символов в число, некоторые числа становятся не верными
   *
   * В ходе эксперементов было выяснено, что проблемными являются только не буквенные символы
   *
   * Так же было выясенно если русскому символу соответствует русский символ в ключе, а не русскому символу - не русский, то все норм
   *
   * import java.nio.charset.StandardCharsets                                        //|import java.nio.charset.StandardCharsets
   * val ar = Array[Byte](-16)                                                       //|ar: Array[Byte] = [B@55d32415
   * val charset = StandardCharsets.UTF_8                                            //|charset: java.nio.charset.Charset = UTF-8
   * val str = new String(ar, charset)                                               //|str: String = ?
   * str.getBytes(charset).mkString(",")                                             //|res0: String = -17,-65,-67
   */
  def var1() {
    def encode(pText: String, pKey: String) = {
      val txt = pText.getBytes(CharsetName)
      println(s"txt (${pText.size}}->${txt.size}}): ${txt.mkString(",")}")

      val key = pKey.getBytes(CharsetName)
      println("key = " + key.mkString(","))

      val res = for (i <- 0 until txt.length) yield (txt(i) ^ key(i % key.length)).toByte
      println("res = " + res.mkString(","))

      new String(res.toArray, CharsetName)
    }

    val en = encode(text, key)
    println(s"encode: $en\n")

    val de = encode(en, key)
    println(s"decode: $de")
  }

  /**
   * Работает с любой расширенной кодировкой
   * Принимает массив байт и возвращает закодированный массив байт
   */
  def var2() {
    def encode(txt: Array[Byte], pKey: String) = {
      println(s"txt: ${txt.mkString(",")}")

      val key = pKey.getBytes(CharsetName)
      println("key = " + key.mkString(","))

      val res = for (i <- 0 until txt.length) yield (txt(i) ^ key(i % key.length)).toByte
      println("res = " + res.mkString(","))

      res.toArray
    }

    val enb = encode(text.getBytes(CharsetName), key)
    println(s"encode: ${enb.mkString(",")}\n")

    val deb = encode(enb, key)
    println(s"decode: ${new String(deb, CharsetName)}")
  }
}

/**
 * Просмотр кодов символов
 */
object StringBytes extends App {
  val CharsetName = StandardCharsets.UTF_8

  val abc = "12.абвгдеёжзиклмнопрстуфхцчшщьъэюя"

  println(abc.getBytes(CharsetName).mkString)

  for (c <- abc) {
    println(s"$c -> ${c.toString.getBytes.mkString}")
  }

}
