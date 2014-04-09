package dataprotection.lab.three.form.main

import scala.swing.{GridBagPanel, Button, CheckBox}
import java.awt.{Insets, Dimension}

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:51
 */

/**
 * Нижняя панель
 */
trait BottomPanel {

  /** Является-ли сообщение числом */
  protected val numberCheckBox = new CheckBox("Number")

  /** Кнопка выхода из программы */
  protected val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, 25)
  }


  //////////////////////////////////////////////////////////////


  protected val BottomPanel = new GridBagPanel {
    val c = new Constraints
    c.insets = new Insets(5, 5, 5, 5)
    c.anchor = GridBagPanel.Anchor.West
    c.weightx = 0.5
    layout(numberCheckBox) = c

    c.anchor = GridBagPanel.Anchor.East
    layout(exitButton) = c
  }

}
