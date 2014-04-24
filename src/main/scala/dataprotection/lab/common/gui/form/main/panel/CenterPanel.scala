package dataprotection.lab.common.gui.form.main.panel

import scala.swing._
import java.awt.{Dimension, Insets}

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:44
 */

/**
 * Средняя панель
 */
trait CenterPanel extends DefaultComponents {

  // Кнопки очистки сообщений
  protected val clearDecryptMessageButton = defaultClearButton()
  protected val clearEncryptMessageButton = defaultClearButton()

  // Поля ввода сообщений
  protected val decryptMessageTextArea = defaultTextArea()
  protected val encryptMessageTextArea = defaultTextArea()

  /** Кнопка шифрования сообщения */
  protected val encryptButton = new Button("Encrypt")
  /** Кнопка рашифрования сообщения */
  protected val decryptButton = new Button("Decrypt")


  //////////////////////////////////////////////////////////////


  // Генерация компонентов по умолчанию
  private def defaultTextArea = () => new TextArea(15, 30) {
    lineWrap = true
  }
  private def defaultScrollPane = (c: Component) => new ScrollPane() {
    viewportView = c
    verticalScrollBarPolicy = ScrollPane.BarPolicy.AsNeeded
  }
  private def defaultClearButton = () => new Button("X") {
    preferredSize = new Dimension(preferredSize.width, 20)
  }

  protected val CenterPanel = new GridBagPanel {
    border = Swing.EtchedBorder

    val c = new Constraints
    c.weightx = 0.25
    c.weighty = 0
    c.insets = new Insets(5, 5, 5, 5)
    c.anchor = GridBagPanel.Anchor.East
    layout(defaultLabel("Decrypt Message")) = c

    c.anchor = GridBagPanel.Anchor.West
    layout(clearDecryptMessageButton) = c

    c.anchor = GridBagPanel.Anchor.East
    layout(defaultLabel("Encrypt Message")) = c

    c.anchor = GridBagPanel.Anchor.West
    layout(clearEncryptMessageButton) = c

    c.gridy = 1
    c.weighty = 1
    c.gridwidth = 2
    c.insets = new Insets(0, 5, 5, 5)
    c.fill = GridBagPanel.Fill.Both
    layout(defaultScrollPane(decryptMessageTextArea)) = c
    layout(defaultScrollPane(encryptMessageTextArea)) = c

    c.gridy = 2
    c.weighty = 0
    layout(encryptButton) = c
    layout(decryptButton) = c
  }

}
