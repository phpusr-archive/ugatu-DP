package dataprotection.lab.three.form.main.panel

import scala.swing._
import java.awt.{Dimension, Insets}

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:21
 */

/**
 * Верхняя панель
 */
trait TopPanel extends DefaultComponents {

  // Поля ввода: p, q, n
  protected val generatePButton = defaultGenerateButton()
  protected val pTextField = defaultTextField()
  protected val generateQButton = defaultGenerateButton()
  protected val qTextField = defaultTextField()
  protected val generateNButton = defaultGenerateButton()
  protected val nTextField = defaultTextField()
  
  /** Кнопка генерации ключей */
  protected val generateKeysButton = new Button("Generate Keys") {
    preferredSize = new Dimension(170, 30)
  }

  /** Кнопка очистки текстовых полей */
  protected val clearAllButton = new Button("Clear all") {
    preferredSize = new Dimension(preferredSize.width, generateKeysButton.preferredSize.height)
  }

  // Поля ввода ключей
  protected val generatePublicKeyButton = defaultGenerateButton()
  protected val publicKeyTextField = defaultTextField()
  protected val generatePrivateKeyButton = defaultGenerateButton()
  protected val privateKeyTextField = defaultTextField()


  //////////////////////////////////////////////////////////////


  // Генерация компонентов по умолчанию
  private def defaultTextField = () => new TextField {
    minimumSize = new Dimension(50, 30)
    preferredSize = new Dimension(150, 30)
  }
  private def defaultGenerateButton = () =>  new Button("Gen")
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

  protected val TopPanel = new GridBagPanel {
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
  }

}
