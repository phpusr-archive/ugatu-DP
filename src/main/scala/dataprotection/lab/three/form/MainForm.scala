package dataprotection.lab.three.form

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
    preferredSize = new Dimension(200, 30)
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
  private def defaultGeneratePanel(labelText: String, button: Button, textField: TextField) = new GridBagPanel {
    // Лейбл
    val c = new Constraints
    c.insets = new Insets(0, 5, 0, 5)
    c.gridwidth = 2
    layout(new Label(labelText)) = c

    // Кнопка и текстовое поле
    c.gridy = 1
    c.gridwidth = 1
    layout(button) = c
    layout(textField) = c
  }

  def top = new MainFrame {

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(15, 5, 5, 5)
        c.weightx = 0.5
        layout(defaultGeneratePanel("p", generatePButton, pTextField)) = c
        layout(defaultGeneratePanel("Public Key", generatePublicKeyButton, publicKeyTextField)) = c

        c.insets = new Insets(5, 5, 5, 5)
        c.gridy = 1
        layout(defaultGeneratePanel("q", generateQButton, qTextField)) = c
        layout(defaultGeneratePanel("Private Key", generatePrivateKeyButton, privateKeyTextField)) = c

        c.insets = new Insets(5, 5, 15, 5)
        c.gridy = 2
        layout(defaultGeneratePanel("n", generateNButton, nTextField)) = c
        c.anchor = GridBagPanel.Anchor.South
        layout(generateKeysButton) = c
      }) = North

      // Центральная панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.weightx = 0.5
        c.weighty = 0
        layout(new Label("Decode Message")) = c
        layout(new Label("Encode Message")) = c

        c.gridy = 1
        c.weighty = 1
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

  listenTo(generateKeysButton)
  listenTo(exitButton)
  listenTo(encodeButton, decodeButton)
  listenTo(generatePButton, generateQButton, generateNButton)
  listenTo(generatePublicKeyButton, generatePrivateKeyButton)

  reactions += {
    // Генерация чисел: p, q, n
    case ButtonClicked(`generatePButton`) =>
      pTextField.text = Prime.generatePrime(RSA.PrimeMaxNumber).toString

    case ButtonClicked(`generateQButton`) =>
      qTextField.text = Prime.generatePrime(RSA.PrimeMaxNumber).toString

    case ButtonClicked(`generateNButton`) =>
      nTextField.text = (p * q).toString

    // Генерация ключей
    case ButtonClicked(`generatePublicKeyButton`) =>
      publicKeyTextField.text = RSA.generatePublicKey(p, q, RSA.PublicKeyMaxNumber).toString

    case ButtonClicked(`generatePrivateKeyButton`) =>
      privateKeyTextField.text = RSA.generatePrivateKey(p, q, publicKey).toString

    // Генерация всех числе и ключей
    case ButtonClicked(`generateKeysButton`) =>
      val (p, q, n, publicKey, privateKey) = RSA.generateKeys()
      pTextField.text = p.toString
      qTextField.text = q.toString
      nTextField.text = n.toString
      publicKeyTextField.text = publicKey.toString
      privateKeyTextField.text = privateKey.toString

    // Шифрование
    case ButtonClicked(`encodeButton`) => if (numberCheckBox.selected) {
      encodeMessageTextArea.text = RSA.encodeNumber(decodeMessageTextArea.text, nTextField.text.toInt, publicKeyTextField.text.toInt)
    } else {
      encodeMessageTextArea.text = RSA.encodeString(decodeMessageTextArea.text, nTextField.text.toInt, publicKeyTextField.text.toInt)
    }

    // Расшифрование
    case ButtonClicked(`decodeButton`) => if (numberCheckBox.selected) {
      decodeMessageTextArea.text = RSA.decodeNumber(encodeMessageTextArea.text, nTextField.text.toInt, privateKeyTextField.text.toInt)
    } else {
      decodeMessageTextArea.text = RSA.decodeString(encodeMessageTextArea.text, nTextField.text.toInt, privateKeyTextField.text.toInt)
    }

    // Выход
    case ButtonClicked(`exitButton`) => System.exit(0)
  }
  
  private def p =  pTextField.text.toInt
  private def q =  qTextField.text.toInt
  private def publicKey = publicKeyTextField.text.toInt

}
