package dataprotection.lab.three.form

import scala.swing._
import java.awt.{Insets, Dimension}

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:11
 */

/**
 * TODO Форма
 */
trait Form extends TopPanel with CenterPanel {


  /** Является-ли сообщение числом */
  protected val numberCheckBox = new CheckBox("Number")

  /** Кнопка выхода из программы */
  protected val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, 25)
  }

  ///////////////////////////////////////////////////////////////////

  /** Форма */
  def top = new MainFrame {
    title = "RSA"

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(TopPanel) = North

      // Центральная панель
      layout(CenterPanel) = Center

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
