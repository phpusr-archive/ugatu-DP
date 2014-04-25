package dataprotection.lab.common.gui.form.main.panel.top

import scala.swing.{Alignment, TextField, Button, GridBagPanel}
import java.awt.{Insets, Dimension}
import dataprotection.lab.common.gui.form.main.panel.DefaultComponents

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 12:35
 */

/**
 * Панель для шифрования методом RC4
 */
trait Rc4TopPanel extends DefaultComponents {

  /** Поле ввода ключа */
  protected val rc4KeyTextField = new TextField {
    preferredSize = new Dimension(300, 30)
    horizontalAlignment = Alignment.Center
  }

  /** Кнопка генерации ключа */
  protected val rc4GenerateKeyButton = new Button("Generate Key")


  //////////////////////////////////////////////////////////////


  protected val Rc4TopPanel = new GridBagPanel {
    visible = false

    val c = new Constraints
    c.insets = new Insets(10, 5, 10, 5)

    layout(defaultLabel("Key:")) = c
    layout(rc4KeyTextField) = c
    layout(rc4GenerateKeyButton) = c
  }
}
