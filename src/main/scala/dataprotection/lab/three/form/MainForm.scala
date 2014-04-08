package dataprotection.lab.three.form

import scala.swing._
import scala.swing.event.ButtonClicked
import dataprotection.lab.three.rsa.RSA

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication {
  /** Кнопка генерации ключей */
  private val generateKeysButton = new Button("Generate Keys")
  /** Кнопка выхода из программы */
  private val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, 25)
  }
  /** Кнопка шифрования сообщения */
  private val encodeButton = new Button("Encode")
  /** Кнопка рашифрования сообщения */
  private val decodeButton = new Button("Decode")

  // Поля ввода ключей
  private val nTextField = defaultTextField
  private val publicKeyTextField = defaultTextField
  private val privateKeyTextField = defaultTextField

  // Поля ввода сообщений
  private val decodeMessageTextArea = defaultTextArea
  private val encodeMessageTextArea = defaultTextArea

  /** Является-ли сообщение числом */
  private val numberCheckBox = new CheckBox("Number")

  // Генерация компонентов по умолчанию
  private def defaultTextField = new TextField {
    preferredSize = new Dimension(150, 30)
  }
  private def defaultTextArea = new TextArea {
    lineWrap = true
  }

  def top = new MainFrame {

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.weightx = 0.333
        layout(new Label("Public Key")) = c
        layout(new Label("n")) = c
        layout(new Label("Private Key")) = c

        c.gridy = 1
        layout(publicKeyTextField) = c
        layout(nTextField) = c
        layout(privateKeyTextField) = c

        c.gridx = 1
        c.gridy = 2
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
    size = new Dimension(600, 400)
  }

  listenTo(generateKeysButton)
  listenTo(exitButton)
  listenTo(encodeButton, decodeButton)

  reactions += {
    case ButtonClicked(`generateKeysButton`) =>
      val (n, publicKey, privateKey) = RSA.generateKeys()
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

}
