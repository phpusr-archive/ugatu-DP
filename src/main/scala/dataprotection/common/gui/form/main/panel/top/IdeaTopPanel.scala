package dataprotection.common.gui.form.main.panel.top

import scala.swing.{Alignment, TextField, Button, GridBagPanel}
import java.awt.{Insets, Dimension}
import dataprotection.common.gui.form.main.panel.DefaultComponents

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 12:35
 */

/**
 * Панель для шифрования методом IDEA
 */
trait IdeaTopPanel extends DefaultComponents {

  /** Поле ввода ключа */
  protected val ideaKeyTextField = new TextField {
    preferredSize = new Dimension(300, 30)
    horizontalAlignment = Alignment.Center
  }

  /** Кнопка генерации ключа */
  protected val ideaGenerateKeyButton = new Button("Generate Key")


  //////////////////////////////////////////////////////////////


  protected val IdeaTopPanel = new GridBagPanel {
    visible = false

    val c = new Constraints
    c.insets = new Insets(10, 5, 10, 5)

    layout(defaultLabel("Key:")) = c
    layout(ideaKeyTextField) = c
    layout(ideaGenerateKeyButton) = c
  }
}
