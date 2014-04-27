package experiment.form

import scala.swing._

/**
 * @author phpusr
 *         Date: 21.04.14
 *         Time: 11:41
 */

/**
 * Демонстрация меню
 */
object MenuTest extends SimpleSwingApplication {

  def top = new MainFrame {

    /*
     * Create a menu bar with a couple of menus and menu items and
     * set the result as this frame's menu bar.
     */
    menuBar = new MenuBar {
      contents += new Menu("A Menu") {
        contents += new MenuItem("An item")
        contents += new MenuItem(Action("An action item") {
          println("Action '"+ title +"' invoked")
        })
        contents += new Separator
        contents += new CheckMenuItem("Check me")
        contents += new CheckMenuItem("Me too!")
        contents += new Separator
        val a = new RadioMenuItem("a")
        val b = new RadioMenuItem("b")
        val c = new RadioMenuItem("c")
        val mutex = new ButtonGroup(a,b,c)
        contents ++= mutex.buttons
      }
      contents += new Menu("Empty Menu")
    }

    size = new Dimension(400, 300)
    centerOnScreen()
  }
}
