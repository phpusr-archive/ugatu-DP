package dataprotection.lab.three.form

import scala.swing._
import scala.swing.event.ButtonClicked
import dataprotection.lab.three.rsa.RSA
import dataprotection.lab.three.prime.Prime
import java.awt.Font
import scala.swing.Font

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication {

  // Поля ввода: p, q, n
  private val generatePButton = defaultGenerateButton
  private val pTextField = defaultTextField
  private val generateQButton = defaultGenerateButton
  private val qTextField = defaultTextField
  private val generateNButton = defaultGenerateButton
  private val nTextField = defaultTextField

  // Поля ввода ключей
  private val generatePublicKeyButton = defaultGenerateButton
  private val publicKeyTextField = defaultTextField
  private val generatePrivateKeyButton = defaultGenerateButton
  private val privateKeyTextField = defaultTextField

  /** Кнопка генерации ключей */
  private val generateKeysButton = new Button("Generate Keys") {
    preferredSize = new Dimension(170, 30)
  }
  /** Кнопка очистки текстовых полей */
  private val clearAllButton = new Button("Clear all") {
    preferredSize = new Dimension(preferredSize.width, generateKeysButton.preferredSize.height)
  }

  // Поля ввода сообщений
  private val decodeMessageTextArea = defaultTextArea
  private val encodeMessageTextArea = defaultTextArea

  /** Кнопка шифрования сообщения */
  private val encodeButton = new Button("Encode")
  /** Кнопка рашифрования сообщения */
  private val decodeButton = new Button("Decode")

  /** Является-ли сообщение числом */
  private val numberCheckBox = new CheckBox("Number")

  /** Кнопка выхода из программы */
  private val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, 25)
  }

  ///////////////////////////////////////////////////////////////////

  // Генерация компонентов по умолчанию
  private def defaultTextField = new TextField {
    preferredSize = new Dimension(150, 30)
  }
  private def defaultTextArea = new TextArea {
    lineWrap = true
    preferredSize = new Dimension(300, 200)
  }
  private def defaultGenerateButton = new Button("Gen")
  private def defaultLabel = (title: String) => new Label(title) {
    font = new Font("Arial", Font.BOLD, 12)
  }
  private def defaultGeneratePanel(labelText: String, button: Button, textField: TextField) = new GridBagPanel {
    // Лейбл
    val c = new Constraints
    c.insets = new Insets(0, 2, 0, 2)
    c.gridwidth = 3
    layout(defaultLabel(labelText)) = c

    // Кнопка генерации и текстовое поле
    c.gridy = 1
    c.gridwidth = 1
    layout(button) = c
    layout(textField) = c

    // Кнопка очистки текстового поля
    val clearButton = new Button(Action("X") { textField.text = "" })
    layout(clearButton) = c
  }

  /** Форма */
  def top = new MainFrame {
    title = "RSA"

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        val insetsLeft = 50
        val insetsRight = 50
        c.insets = new Insets(15, insetsLeft, 5, insetsRight)
        layout(defaultGeneratePanel("p", generatePButton, pTextField)) = c
        layout(defaultGeneratePanel("Public Key", generatePublicKeyButton, publicKeyTextField)) = c

        c.gridy = 1
        c.insets = new Insets(5, insetsLeft, 5, insetsRight)
        layout(defaultGeneratePanel("q", generateQButton, qTextField)) = c
        layout(defaultGeneratePanel("Private Key", generatePrivateKeyButton, privateKeyTextField)) = c

        c.gridy = 2
        c.insets = new Insets(5, insetsLeft, 15, insetsRight)
        layout(defaultGeneratePanel("n", generateNButton, nTextField)) = c

        c.insets = new Insets(5, insetsLeft, 10, insetsRight)
        c.anchor = GridBagPanel.Anchor.South
        layout(new FlowPanel {
          contents += generateKeysButton
          contents += clearAllButton
        }) = c
      }) = North

      // Центральная панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.weightx = 0.5
        c.weighty = 0
        c.insets = new Insets(5, 5, 0, 5)
        layout(defaultLabel("Decode Message")) = c
        layout(defaultLabel("Encode Message")) = c

        c.gridy = 1
        c.weighty = 1
        c.insets = new Insets(0, 5, 5, 5)
        c.fill = GridBagPanel.Fill.Both
        layout(decodeMessageTextArea) = c
        layout(encodeMessageTextArea) = c

        c.gridy = 2
        c.weighty = 0
        layout(encodeButton) = c
        layout(decodeButton) = c
      }) = Center

      // Нижняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.anchor = GridBagPanel.Anchor.West
        c.weightx = 0.5
        layout(numberCheckBox) = c

        c.anchor = GridBagPanel.Anchor.East
        layout(exitButton) = c
      }) = South
    }

    peer.setLocationRelativeTo(null)
  }

  // Обработчики событий формы
  listenTo(generatePButton, generateQButton, generateNButton)
  listenTo(generatePublicKeyButton, generatePrivateKeyButton)
  listenTo(generateKeysButton, clearAllButton)
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

    // Очистка всех полей ввода
    case ButtonClicked(`clearAllButton`) =>
      clearP(); clearQ(); clearN(); clearPublicKey(); clearPrivateKey()

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

  // Получение значений из формы
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
  private def encodeMessage = encodeMessageTextArea.text

}
