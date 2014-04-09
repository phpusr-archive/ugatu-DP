package dataprotection.lab.three.form

import scala.swing.Label
import java.awt.Font

/**
 * @author phpusr
 *         Date: 09.04.14
 *         Time: 16:30
 */

trait Default {
  // Генерация компонентов по умолчанию
  protected def defaultLabel = (title: String) => new Label(title) {
    font = new Font("Arial", Font.BOLD, 12)
  }
}
