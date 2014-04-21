package dataprotection.lab.common.gui.form.main

import scala.swing._
import scala.swing.event.ButtonClicked
import dataprotection.lab.three.rsa.RSA
import dataprotection.lab.three.prime.Prime
import dataprotection.lab.three.types.RsaTrait
import javax.swing.JFrame
import dataprotection.lab.common.gui.form.main.panel.top.{GostTopPanel, RsaTopPanel}
import dataprotection.lab.common.gui.form.main.panel.{CenterPanel, BottomPanel}
import dataprotection.lab.one.gost2814789.tools.GostHelper
import org.dyndns.phpusr.util.log.Logger

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication with GostTopPanel with RsaTrait with RsaTopPanel with CenterPanel with BottomPanel {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  // Элементы меню методов шифрования
  private val gostMenuItem = new RadioMenuItem("GOST-28147-89")
  private val rsaMenuItem = new RadioMenuItem("RSA")

  // Frame, нужен для вызова pack()
  private var gPeer: JFrame = null

  /** Список панелей методов шифрования */
  private val topPanels = List(GostTopPanel, RsaTopPanel)

  /** Форма */
  def top = new MainFrame {
    gPeer = peer
    title = "RSA"

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Меню
      menuBar = new MenuBar {
        // Выбор метода шифрования
        contents += new Menu("Encryption method") {
          val mutex = new ButtonGroup(gostMenuItem, rsaMenuItem)
          contents ++= mutex.buttons
        }
      }

      // Верхняя панель
      layout(new FlowPanel {
        contents += RsaTopPanel
        contents += GostTopPanel
      }) = North

      // Центральная панель
      layout(CenterPanel) = Center

      // Нижняя панель
      layout(BottomPanel) = South
    }

    // Init form
    changePanel(GostTopPanel)
    centerOnScreen()
  }

  // Обработчики событий формы
  listenTo(gostMenuItem, rsaMenuItem)

  listenTo(gostGenerateKeyButton)

  listenTo(generatePButton, generateQButton, generateNButton)
  listenTo(generatePublicKeyButton, generatePrivateKeyButton)
  listenTo(generateKeysButton, clearAllButton)

  listenTo(clearEncodeMessageButton, clearDecodeMessageButton)
  listenTo(encodeButton, decodeButton)

  listenTo(exitButton)

  reactions += {
    // Методы шифрования
    case ButtonClicked(`gostMenuItem`) =>
      changePanel(GostTopPanel)
    case ButtonClicked(`rsaMenuItem`) =>
      changePanel(RsaTopPanel)

    //--------------------- begin GOST ---------------------//

    case ButtonClicked(`gostGenerateKeyButton`) =>
      val (keySeq, keyHex) = GostHelper.generateKey()
      gostKeyTextField.text = keyHex

    //--------------------- end GOST ---------------------//

    //--------------------- begin RSA ---------------------//

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

    //--------------------- end RSA ---------------------//

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
  
  /** Показ определенной панели метода шифрования и скрытие остальных */
  private def changePanel(panel: Panel) {
    //TODO смена заголовка программы

    // Смена видимой панели
    topPanels.foreach { p =>
      p.visible = p == panel
    }

    // Чек-бокс шифрования числа доступен только для RSA
    numberCheckBox.visible = panel == RsaTopPanel

    // Выровнить размер окна под компонеты
    gPeer.pack()
  }

  // Получение и установка значений на форме
  private def p = pTextField.text.toRsaNumber
  private def clearP() = pTextField.text = ""
  private def q = qTextField.text.toRsaNumber
  private def clearQ() = qTextField.text = ""
  private def n = nTextField.text.toRsaNumber
  private def clearN() = nTextField.text = ""

  private def publicKey = publicKeyTextField.text.toRsaNumber
  private def clearPublicKey() = publicKeyTextField.text = ""
  private def privateKey = privateKeyTextField.text.toRsaNumber
  private def clearPrivateKey() = privateKeyTextField.text = ""

  private def decodeMessage = decodeMessageTextArea.text
  private def clearDecodeMessage() = decodeMessageTextArea.text = ""
  private def encodeMessage = encodeMessageTextArea.text
  private def clearEncodeMessage() = encodeMessageTextArea.text = ""

}
