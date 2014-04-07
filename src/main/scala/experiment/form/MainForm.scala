package experiment.form

import scala.swing._
import java.awt.Color
import javax.swing.SpringLayout.Constraints
import scala.swing.event.ButtonClicked
import dataprotection.lab.three.rsa.RSA

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */

/**
 * Главная форма
 */
object MainForm extends SimpleSwingApplication {
  /** Кнопка генерации ключей */
  private val generateKeysButton = new Button("Generate Keys")
  /** Кнопка выхода из программы */
  private val exitButton = new Button("Exit") {
    preferredSize = new Dimension(150, 25)
  }

  // Поля ввода ключей
  private val nTextField = defaultTextField
  private val publicKeyTextField = defaultTextField
  private val privateKeyTextField = defaultTextField

  /** Генерация текстового поля по умолчанию */
  private def defaultTextField = new TextField {
    preferredSize = new Dimension(150, 20)
  }

  def top = new MainFrame {

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.weightx = 0.333
        layout(new Label("Public Key")) = c
        layout(new Label("n")) = c
        layout(new Label("Private Key")) = c

        c.gridy = 1
        layout(publicKeyTextField) = c
        layout(nTextField) = c
        layout(privateKeyTextField) = c

        c.gridx = 1
        c.gridy = 2
        layout(generateKeysButton) = c
      }) = North

      // Центральная панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.weightx = 0.5
        c.weighty = 0
        layout(new Label("Decode Message")) = c
        layout(new Label("Encode Message")) = c

        c.gridy = 1
        c.weighty = 1
        c.fill = GridBagPanel.Fill.Both
        layout(new TextArea) = c
        layout(new TextArea) = c

        c.gridy = 2
        c.weighty = 0
        layout(new Button("Encode")) = c
        layout(new Button("Decode")) = c
      }) = Center

      // Нижняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.anchor = GridBagPanel.Anchor.West
        c.weightx = 0.5
        layout(new CheckBox("Number")) = c

        c.anchor = GridBagPanel.Anchor.East
        layout(exitButton) = c
      }) = South
    }

    peer.setLocationRelativeTo(null)
    size = new Dimension(600, 400)
  }

  listenTo(generateKeysButton)
  listenTo(exitButton)
  reactions += {
    case ButtonClicked(`generateKeysButton`) =>
      val (n, publicKey, privateKey) = RSA.generateKeys()
      nTextField.text = n.toString
      publicKeyTextField.text = publicKey.toString
      privateKeyTextField.text = privateKey.toString

    case ButtonClicked(`exitButton`) => System.exit(0)
  }

}
