package dataprotection.lab.three.form

import scala.swing._
import java.awt.Dimension

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:11
 */

/**
 * TODO Форма
 */
trait Form extends TopPanel with CenterPanel with BottomPanel {


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
      layout(BottomPanel) = South
    }

    centerOnScreen()
  }

}
