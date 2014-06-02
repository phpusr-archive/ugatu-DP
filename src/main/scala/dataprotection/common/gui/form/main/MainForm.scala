package dataprotection.common.gui.form.main

import scala.swing._
import dataprotection.lab.three.rsa.RSA
import dataprotection.lab.three.prime.Prime
import dataprotection.lab.three.types.RsaTrait
import javax.swing.JFrame
import dataprotection.common.gui.form.main.panel.top.{IdeaTopPanel, Rc4TopPanel, GostTopPanel, RsaTopPanel}
import dataprotection.common.gui.form.main.panel.{CenterPanel, BottomPanel}
import dataprotection.lab.one.gost2814789.tools.GostHelper
import scala.swing.event.ButtonClicked
import org.dyndns.phpusr.util.log.Logger
import dataprotection.common.gui.form.main.EncryptMethod._
import dataprotection.lab.one.gost2814789.Gost
import dataprotection.lab.two.rc4.RC4
import dataprotection.rgr.idea.IDEA
import org.dyndns.phpusr.bitnumber.BitNumber

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication with GostTopPanel with Rc4TopPanel with RsaTrait with RsaTopPanel with IdeaTopPanel with CenterPanel with BottomPanel {

  /** Логирование */
  private val logger = Logger(infoEnable = true, debugEnable = true, traceEnable = true)

  // Элементы меню методов шифрования
  private val gostMenuItem = new RadioMenuItem(GOST_28147_89_METHOD.name)
  private val rc4MenuItem = new RadioMenuItem(RC4_METHOD.name)
  private val rsaMenuItem = new RadioMenuItem(RSA_METHOD.name)
  private val ideaMenuItem = new RadioMenuItem(IDEA_METHOD.name)

  // Frame, нужен для вызова pack()
  private var gPeer: JFrame = null

  /** Список панелей методов шифрования */
  private val topPanels = List(GostTopPanel, Rc4TopPanel, RsaTopPanel, IdeaTopPanel)

  /** Текущий метод шифрования */
  private var currentMethodEncrypt: EncryptMethod = null

  /** Форма */
  def top = new MainFrame {
    gPeer = peer

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Меню
      menuBar = new MenuBar {
        // Выбор метода шифрования
        contents += new Menu("Encryption method") {
          val mutex = new ButtonGroup(gostMenuItem, rc4MenuItem, rsaMenuItem, ideaMenuItem)
          contents ++= mutex.buttons
        }
      }

      // Верхняя панель
      layout(new FlowPanel {
        contents += GostTopPanel
        contents += Rc4TopPanel
        contents += RsaTopPanel
        contents += IdeaTopPanel
      }) = North

      // Центральная панель
      layout(CenterPanel) = Center

      // Нижняя панель
      layout(BottomPanel) = South
    }

    // Init form
    changeEncryptMethod(IDEA_METHOD)
    ideaMenuItem.selected = true
    centerOnScreen()
  }

  // Обработчики событий формы
  listenTo(gostMenuItem, rc4MenuItem, rsaMenuItem, ideaMenuItem)

  listenTo(gostGenerateKeyButton, rc4GenerateKeyButton, ideaGenerateKeyButton)

  listenTo(generatePButton, generateQButton, generateNButton)
  listenTo(generatePublicKeyButton, generatePrivateKeyButton)
  listenTo(generateKeysButton, clearAllButton)

  listenTo(clearEncryptMessageButton, clearDecryptMessageButton)
  listenTo(encryptButton, decryptButton)

  listenTo(exitButton)

  reactions += {
    // Методы шифрования
    case ButtonClicked(`gostMenuItem`) => changeEncryptMethod(GOST_28147_89_METHOD)
    case ButtonClicked(`rc4MenuItem`) => changeEncryptMethod(RC4_METHOD)
    case ButtonClicked(`rsaMenuItem`) => changeEncryptMethod(RSA_METHOD)
    case ButtonClicked(`ideaMenuItem`) => changeEncryptMethod(IDEA_METHOD)

    //--------------------- begin GOST ---------------------//

    case ButtonClicked(`gostGenerateKeyButton`) =>
      val (keySeq, keyHex) = GostHelper.generateKey()
      gostKeyTextField.text = keyHex

    //--------------------- end GOST ---------------------//


    //--------------------- begin RC4 ---------------------//

    case ButtonClicked(`rc4GenerateKeyButton`) =>
      val key = RC4.generateKey(10).mkString(RC4.Splitter)
      rc4KeyTextField.text = key

    //--------------------- begin RC4 ---------------------//


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


    //--------------------- begin IDEA ---------------------//

    case ButtonClicked(`ideaGenerateKeyButton`) =>
      ideaKeyTextField.text = IDEA.generateKey().toHexStr

    //--------------------- begin IDEA ---------------------//


    // Очистка полей ввода
    case ButtonClicked(`clearAllButton`) =>
      clearP(); clearQ(); clearN(); clearPublicKey(); clearPrivateKey()
    case ButtonClicked(`clearDecryptMessageButton`) => clearDecryptMessage()
    case ButtonClicked(`clearEncryptMessageButton`) => clearEncryptMessage()

    // Шифрование
    case ButtonClicked(`encryptButton`) =>
      currentMethodEncrypt match {
        case GOST_28147_89_METHOD => gostEncrypt()
        case RC4_METHOD => rc4Encrypt()
        case RSA_METHOD => rsaEncrypt()
        case IDEA_METHOD => ideaEncrypt()
      }


    // Расшифрование
    case ButtonClicked(`decryptButton`) =>
      currentMethodEncrypt match {
        case GOST_28147_89_METHOD => gostDecrypt()
        case RC4_METHOD => rc4Decrypt()
        case RSA_METHOD => rsaDecrypt()
        case IDEA_METHOD => ideaDecrypt()
      }

    // Выход
    case ButtonClicked(`exitButton`) => System.exit(0)
  }

  /** Шифрование сообщения методом ГОСТ-28147-89 */
  private def gostEncrypt() {
    val key = GostHelper.keyHexToKeyArray(gostKeyHex)
    val messageBlocks = GostHelper.stringToBlockArray(decryptMessage)
    val encryptBlocks = Gost.encryptBlockArray(messageBlocks, key)
    encryptMessageTextArea.text = GostHelper.blockArrayToHexString(encryptBlocks)
  }

  /** Расшифрование сообщения методом ГОСТ-28147-89 */
  private def gostDecrypt() {
    val key = GostHelper.keyHexToKeyArray(gostKeyHex)
    val encryptMessageBlocks = GostHelper.hexStringToBlockArray(encryptMessage)
    val decryptMessageBlocks = Gost.decryptBlockArray(encryptMessageBlocks, key)
    decryptMessageTextArea.text = GostHelper.blockArrayToString(decryptMessageBlocks)
  }

  /** Шифрование сообщения методом RC4 */
  private def rc4Encrypt() {
    val key = rc4Key.split(RC4.Splitter).map(_.toByte)
    val data = decryptMessage.getBytes(RC4.CharsetName)
    val encryptData = RC4.encrypt(data, key)
    encryptMessageTextArea.text = encryptData.mkString(RC4.Splitter)
  }

  /** Расшифрование сообщения методом RC4 */
  private def rc4Decrypt() {
    val key = rc4Key.split(RC4.Splitter).map(_.toByte)
    val encryptData = encryptMessage.split(RC4.Splitter).map(_.toByte)
    val decryptData = RC4.decrypt(encryptData, key)
    decryptMessageTextArea.text = new String(decryptData, RC4.CharsetName)
  }

  /** Шифрование сообщения методом RSA */
  private def rsaEncrypt() {
    if (numberCheckBox.selected) {
      encryptMessageTextArea.text = RSA.encryptNumber(decryptMessage, n, publicKey)
    } else {
      encryptMessageTextArea.text = RSA.encryptString(decryptMessage, n, publicKey)
    }
  }

  /** Расшифрование сообщения методом RSA */
  private def rsaDecrypt() {
    if (numberCheckBox.selected) {
      decryptMessageTextArea.text = RSA.decryptNumber(encryptMessage, n, privateKey)
    } else {
      decryptMessageTextArea.text = RSA.decryptString(encryptMessage, n, privateKey)
    }
  }

  /** Шифрование сообщения методом IDEA */
  private def ideaEncrypt() {
    val encryptIdea = new IDEA(ideaKey, true)

    val data = BitNumber(decryptMessage.getBytes) //TODO charset
    val Aggregate = '^'.toByte
    val aggregateCount = (IDEA.BlockSize - data.size % IDEA.BlockSize) / 8
    val agr = (for (i <- 1 to aggregateCount) yield Aggregate).toArray
    data.join(BitNumber(agr))

    encryptMessageTextArea.text = encryptIdea.processBlocks(data).toHexStr
  }

  /** Расшифрование сообщения методом IDEA */
  private def ideaDecrypt() {
    val decryptIdea = new IDEA(ideaKey, false)

    val a = encryptMessage.sliding(8, 8).map(java.lang.Long.parseLong(_, 16).toInt).toArray //TODO improve
    val data = BitNumber(a)

    val bytes = decryptIdea.processBlocks(data).getBytes
    decryptMessageTextArea.text = new String(bytes) //TODO charset
    //TODO убрать заполнитель
  }

  /** Показ определенной панели метода шифрования и скрытие остальных */
  private def changeEncryptMethod(method: EncryptMethod) {

    // Выбор панели
    val panel = method match {
      case GOST_28147_89_METHOD => GostTopPanel
      case RC4_METHOD => Rc4TopPanel
      case RSA_METHOD => RsaTopPanel
      case IDEA_METHOD => IdeaTopPanel
    }

    // Установка текущего метода шифрования
    currentMethodEncrypt = method
    // Изменение заголовка
    gPeer.setTitle(method.name)
    // Смена видимой панели
    topPanels.foreach { p => p.visible = p == panel }
    // Чек-бокс шифрования числа доступен только для RSA
    numberCheckBox.visible = panel == RsaTopPanel
    // Выровнить размер окна под компонеты
    gPeer.pack()
  }

  // Получение и установка значений на форме

  // RSA
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

  // ГОСТ 28147-89
  private def gostKeyHex = gostKeyTextField.text

  // RC4
  private def rc4Key = rc4KeyTextField.text

  // IDEA
  private def ideaKey = {
    val keyArray = ideaKeyTextField.text.sliding(8, 8).map(java.lang.Long.parseLong(_, 16).toInt).toArray
    BitNumber(keyArray)
  }

  private def decryptMessage = decryptMessageTextArea.text
  private def clearDecryptMessage() = decryptMessageTextArea.text = ""
  private def encryptMessage = encryptMessageTextArea.text
  private def clearEncryptMessage() = encryptMessageTextArea.text = ""

}
