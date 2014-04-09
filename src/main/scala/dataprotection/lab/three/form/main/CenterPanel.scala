package dataprotection.lab.three.form.main

import scala.swing.{TextArea, Button, Swing, GridBagPanel}
import java.awt.{Dimension, Insets}

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:44
 */

trait CenterPanel extends Default {

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

  // Генерация компонентов по умолчанию
  private def defaultTextArea = () => new TextArea {
    lineWrap = true
    preferredSize = new Dimension(350, 200)
  }

  protected val CenterPanel = new GridBagPanel {
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
  }

}
