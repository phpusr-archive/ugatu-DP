package dataprotection.common.gui.form.main.panel

import scala.swing.Label
import java.awt.Font

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:30
 */

/**
 * Генерация компонентов по умолчанию
 */
trait DefaultComponents {
  
  protected def defaultLabel = (title: String) => new Label(title) {
    font = new Font("Arial", Font.BOLD, 12)
  }
  
}
