package dataprotection.lab.common.run

import javax.swing.{JFrame, UIManager}
import dataprotection.lab.common.gui.form.main.MainForm


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
  val lookAndFeelClassName = 2 match {
    case 0 => UIManager.getCrossPlatformLookAndFeelClassName
    case 1 => UIManager.getSystemLookAndFeelClassName
    case 2 => "javax.swing.plaf.nimbus.NimbusLookAndFeel"
  }
  UIManager.setLookAndFeel(lookAndFeelClassName)

  // Установка панели с кнопками (свернуть, на весь экран закыть) от темы оформления
  JFrame.setDefaultLookAndFeelDecorated(true)

  // Создание и запуск главной формы
  MainForm.main(args)
}
