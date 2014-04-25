package dataprotection.lab.common.gui.form.main

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 13:34
 */

/** Метод шифрования */
object EncryptMethod extends Enumeration {
  
  case class EncryptMethod(name: String)

  /** ГОСТ 28147-89 */
  val GOST_28147_89_METHOD = EncryptMethod("GOST 28147-89")

  /** RC4 */
  val RC4_METHOD = EncryptMethod("RC4")
  
  /** RSA */
  val RSA_METHOD = EncryptMethod("RSA")
}
