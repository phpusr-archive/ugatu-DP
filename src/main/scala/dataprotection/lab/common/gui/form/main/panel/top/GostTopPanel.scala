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
 * Панель для шифрования методом ГОСТ-28147-89
 */
trait GostTopPanel extends DefaultComponents {

  /** Поле ввода ключа */
  protected val gostKeyTextField = new TextField {
    preferredSize = new Dimension(470, 30)
    horizontalAlignment = Alignment.Center
  }

  /** Кнопка генерации ключа */
  protected val gostGenerateKeyButton = new Button("Generate Key")


  //////////////////////////////////////////////////////////////


  protected val GostTopPanel = new GridBagPanel {
    visible = false

    val c = new Constraints
    c.insets = new Insets(10, 5, 10, 5)

    layout(defaultLabel("Key:")) = c
    layout(gostKeyTextField) = c
    layout(gostGenerateKeyButton) = c
  }
}
