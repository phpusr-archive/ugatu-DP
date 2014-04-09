package dataprotection.lab.three.form

import scala.swing._
import java.awt.{Insets, Font, Dimension}

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:11
 */

/**
 * TODO Форма
 */
trait Form {

  // Поля ввода: p, q, n
  protected val generatePButton = defaultGenerateButton()
  protected val pTextField = defaultTextField()
  protected val generateQButton = defaultGenerateButton()
  protected val qTextField = defaultTextField()
  protected val generateNButton = defaultGenerateButton()
  protected val nTextField = defaultTextField()

  // Поля ввода ключей
  protected val generatePublicKeyButton = defaultGenerateButton()
  protected val publicKeyTextField = defaultTextField()
  protected val generatePrivateKeyButton = defaultGenerateButton()
  protected val privateKeyTextField = defaultTextField()

  /** Кнопка генерации ключей */
  protected val generateKeysButton = new Button("Generate Keys") {
    preferredSize = new Dimension(170, 30)
  }
  /** Кнопка очистки текстовых полей */
  protected val clearAllButton = new Button("Clear all") {
    preferredSize = new Dimension(preferredSize.width, generateKeysButton.preferredSize.height)
  }

  // Кнопки очистки сообщений
  protected val clearDecodeMessageButton = new Button("X")
  protected val clearEncodeMessageButton = new Button("X")

  // Поля ввода сообщений
  protected val decodeMessageTextArea = defaultTextArea()
  protected val encodeMessageTextArea = defaultTextArea()

  /** Кнопка шифрования сообщения */
  protected val encodeButton = new Button("Encode")
  /** Кнопка рашифрования сообщения */
  protected val decodeButton = new Button("Decode")

  /** Является-ли сообщение числом */
  protected val numberCheckBox = new CheckBox("Number")

  /** Кнопка выхода из программы */
  protected val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, 25)
  }

  ///////////////////////////////////////////////////////////////////

  // Генерация компонентов по умолчанию
  private def defaultTextField = () => new TextField {
    minimumSize = new Dimension(50, 30)
    preferredSize = new Dimension(150, 30)
  }
  private def defaultTextArea = () => new TextArea {
    lineWrap = true
    preferredSize = new Dimension(350, 200)
  }
  private def defaultGenerateButton = () =>  new Button("Gen")
  private def defaultLabel = (title: String) => new Label(title) {
    font = new Font("Arial", Font.BOLD, 12)
  }
  private def defaultGeneratePanel = (labelText: String, button: Button, textField: TextField) => new GridBagPanel {
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
        c.insets = new Insets(15, 0, 5, insetsRight)
        layout(defaultGeneratePanel("p", generatePButton, pTextField)) = c

        c.insets = new Insets(15, insetsLeft, 5, 0)
        layout(defaultGeneratePanel("Public Key", generatePublicKeyButton, publicKeyTextField)) = c

        c.gridy = 1
        c.insets = new Insets(5, 0, 5, insetsRight)
        layout(defaultGeneratePanel("q", generateQButton, qTextField)) = c

        c.insets = new Insets(5, insetsLeft, 5, 0)
        layout(defaultGeneratePanel("Private Key", generatePrivateKeyButton, privateKeyTextField)) = c

        c.gridy = 2
        c.insets = new Insets(5, 0, 15, insetsRight)
        layout(defaultGeneratePanel("n", generateNButton, nTextField)) = c

        c.insets = new Insets(5, insetsLeft, 10, 0)
        c.anchor = GridBagPanel.Anchor.South
        layout(new FlowPanel {
          contents += generateKeysButton
          contents += clearAllButton
        }) = c
      }) = North

      // Центральная панель
      layout(new GridBagPanel {
        border = Swing.EtchedBorder

        val c = new Constraints
        c.weightx = 0.25
        c.weighty = 0
        c.insets = new Insets(5, 5, 0, 5)
        c.anchor = GridBagPanel.Anchor.East
        layout(defaultLabel("Decode Message")) = c

        c.anchor = GridBagPanel.Anchor.West
        layout(clearDecodeMessageButton) = c

        c.anchor = GridBagPanel.Anchor.East
        layout(defaultLabel("Encode Message")) = c

        c.anchor = GridBagPanel.Anchor.West
        layout(clearEncodeMessageButton) = c

        c.gridy = 1
        c.weighty = 1
        c.gridwidth = 2
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

    centerOnScreen()
  }

}
