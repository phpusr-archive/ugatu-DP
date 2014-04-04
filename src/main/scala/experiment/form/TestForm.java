package experiment.form;

import javax.swing.*;

/**
 * @author phpusr
 *         Date: 04.04.14
 *         Time: 11:31
 */

public class TestForm extends JFrame {
    private JPanel rootPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JPanel keysPanel;
    private JPanel btnsPanel;
    private JPanel messagePanel;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton generateKeysButton;
    private JButton encodeButton;
    private JButton decodeButton;
    private JCheckBox numberCheckBox;
    private JButton exitButton;

    public TestForm () {
        super("Main Form" );
        setContentPane(rootPanel);
        setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);
        pack();
        //setSize(600, 500);

        // Actions...

        setVisible (true);
    }


    public static void main(String[] args) {
        new TestForm();
    }
}
