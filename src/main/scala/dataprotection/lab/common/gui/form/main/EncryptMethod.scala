package dataprotection.lab.common.gui.form.main

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 13:34
 */

/** Метод шифрования */
object EncryptMethod extends Enumeration {
  
  type EncryptMethod = Value

  /** ГОСТ-28147-89 */
  val GOST_28_147_89_METHOD = Value
  
  /** RSA */
  val RSA_METHOD = Value
}
