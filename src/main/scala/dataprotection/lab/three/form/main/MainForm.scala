package dataprotection.lab.three.form.main

import scala.swing._
import scala.swing.event.ButtonClicked
import dataprotection.lab.three.rsa.RSA
import dataprotection.lab.three.prime.Prime

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication with TopPanel with CenterPanel with BottomPanel {

  /** Форма */
  def top = new MainFrame {
    title = "RSA"

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(TopPanel) = North

      // Центральная панель
      layout(CenterPanel) = Center

      // Нижняя панель
      layout(BottomPanel) = South
    }

    centerOnScreen()
  }

  // Обработчики событий формы
  listenTo(generatePButton, generateQButton, generateNButton)
  listenTo(generatePublicKeyButton, generatePrivateKeyButton)
  listenTo(generateKeysButton, clearAllButton)

  listenTo(clearEncodeMessageButton, clearDecodeMessageButton)
  listenTo(encodeButton, decodeButton)

  listenTo(exitButton)

  reactions += {
    // Генерация чисел: p, q, n
    case ButtonClicked(`generatePButton`) =>
      clearN(); clearPublicKey(); clearPrivateKey()
      pTextField.text = Prime.generatePrime(RSA.PrimeMaxNumber).toString

    case ButtonClicked(`generateQButton`) =>
      clearN(); clearPublicKey(); clearPrivateKey()
      qTextField.text = Prime.generatePrime(RSA.PrimeMaxNumber).toString

    case ButtonClicked(`generateNButton`) =>
      clearPublicKey(); clearPrivateKey()
      nTextField.text = (p * q).toString

    // Генерация ключей
    case ButtonClicked(`generatePublicKeyButton`) =>
      clearPrivateKey()
      publicKeyTextField.text = RSA.generatePublicKey(p, q, RSA.PublicKeyMaxNumber).toString

    case ButtonClicked(`generatePrivateKeyButton`) =>
      privateKeyTextField.text = RSA.generatePrivateKey(p, q, publicKey).toString

    // Генерация всех чисел и ключей
    case ButtonClicked(`generateKeysButton`) =>
      val (p, q, n, publicKey, privateKey) = RSA.generateKeys()
      pTextField.text = p.toString
      qTextField.text = q.toString
      nTextField.text = n.toString
      publicKeyTextField.text = publicKey.toString
      privateKeyTextField.text = privateKey.toString

    // Очистка полей ввода
    case ButtonClicked(`clearAllButton`) =>
      clearP(); clearQ(); clearN(); clearPublicKey(); clearPrivateKey()
    case ButtonClicked(`clearDecodeMessageButton`) => clearDecodeMessage()
    case ButtonClicked(`clearEncodeMessageButton`) => clearEncodeMessage()

    // Шифрование
    case ButtonClicked(`encodeButton`) => if (numberCheckBox.selected) {
      encodeMessageTextArea.text = RSA.encodeNumber(decodeMessage, n, publicKey)
    } else {
      encodeMessageTextArea.text = RSA.encodeString(decodeMessage, n, publicKey)
    }

    // Расшифрование
    case ButtonClicked(`decodeButton`) => if (numberCheckBox.selected) {
      decodeMessageTextArea.text = RSA.decodeNumber(encodeMessage, n, privateKey)
    } else {
      decodeMessageTextArea.text = RSA.decodeString(encodeMessage, n, privateKey)
    }

    // Выход
    case ButtonClicked(`exitButton`) => System.exit(0)
  }

  // Получение и установка значений на форме
  private def p = pTextField.text.toInt
  private def clearP() = pTextField.text = ""
  private def q = qTextField.text.toInt
  private def clearQ() = qTextField.text = ""
  private def n = nTextField.text.toInt
  private def clearN() = nTextField.text = ""

  private def publicKey = publicKeyTextField.text.toInt
  private def clearPublicKey() = publicKeyTextField.text = ""
  private def privateKey = privateKeyTextField.text.toInt
  private def clearPrivateKey() = privateKeyTextField.text = ""

  private def decodeMessage = decodeMessageTextArea.text
  private def clearDecodeMessage() = decodeMessageTextArea.text = ""
  private def encodeMessage = encodeMessageTextArea.text
  private def clearEncodeMessage() = encodeMessageTextArea.text = ""

}
