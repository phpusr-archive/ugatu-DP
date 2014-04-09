package dataprotection.lab.three.run

import dataprotection.lab.three.form.main.MainForm
import javax.swing.UIManager


/**
 * @author phpusr
 *         Date: 03.04.14
 *         Time: 15:33
 */

/**
 * Запуск программы
 */
object Main extends App {
  // Установка темы оформления
  UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel")
  // Создание и запуск главной формы
  MainForm.main(args)
}
