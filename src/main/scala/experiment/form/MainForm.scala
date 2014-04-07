package experiment.form

import scala.swing._
import javax.swing.BorderFactory
import java.awt.{Color, Paint}
import com.sun.javafx.sg.Border
import javax.swing.SpringLayout.Constraints

/**
 * @author phpusr
 *         Date: 05.04.14
 *         Time: 13:24
 */
object MainForm extends SimpleSwingApplication {

  def top = new MainFrame {

    contents = new BorderPanel {
      import BorderPanel.Position._

      // Верхняя панель
      layout(new GridBagPanel {
        val c = new Constraints
        c.insets = new Insets(5, 5, 5, 5)
        c.weightx = 0.333
        layout(new Label("Public Key")) = c
        layout(new Label("Public Key")) = c
        layout(new Label("Public Key")) = c

        c.gridy = 1
        def text = new TextField {
          preferredSize = new Dimension(150, 20)
        }
        layout(text) = c
        layout(text) = c
        layout(text) = c

        c.gridx = 1
        c.gridy = 2
        layout(new Button("Generate Keys")) = c
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
        layout(new Button("Exit") {
          preferredSize = new Dimension(150, 25)
        }) = c
      }) = South
    }

    peer.setLocationRelativeTo(null)
    size = new Dimension(600, 400)
  }

}
