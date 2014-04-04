package dataprotection.lab.three.form;

import dataprotection.lab.three.rsa.RSA;
import scala.Tuple3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author phpusr
 *         Date: 04.04.14
 *         Time: 11:31
 */

public class TestForm extends JFrame {
    private JPanel rootPanel;
    private JPanel keysPanel;
    private JPanel btnsPanel;
    private JPanel messagePanel;

    private JTextField publicKeyTextField;
    private JTextField nTextField;
    private JTextField privateKeyTextField;
    private JTextArea decodeMessagetextArea;
    private JTextArea encodeMessagetextArea;

    private JCheckBox numberCheckBox;

    private JButton generateKeysButton;
    private JButton encodeButton;
    private JButton decodeButton;
    private JButton exitButton;

    public TestForm () {
        super("RSA");
        setContentPane(rootPanel);
        setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        initListeners();

        setVisible(true);
    }

    // Инициализация обработчиков событий
    private void initListeners() {
        // Генерация ключей
        generateKeysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tuple3<Object,Object,Object> keys = RSA.generateKeys();
                nTextField.setText(keys._1().toString());
                publicKeyTextField.setText(keys._2().toString());
                privateKeyTextField.setText(keys._3().toString());
            }
        });

        // Шифрование
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encodeMessage = null;
                if (isNumberMessage()) {
                    encodeMessage = RSA.encodeNumber(getDecodeMessage(), getN(), getPublicKey());
                } else {
                    encodeMessage = RSA.encodeString(getDecodeMessage(), getN(), getPublicKey());
                }

                encodeMessagetextArea.setText(encodeMessage);
            }
        });

        // Расшифрование
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String decodeMessage = null;
                if (isNumberMessage()) {
                    decodeMessage = RSA.decodeNumber(getEncodeMessage(), getN(), getPrivateKey());
                } else {
                    decodeMessage = RSA.decodeString(getEncodeMessage(), getN(), getPrivateKey());
                }

                decodeMessagetextArea.setText(decodeMessage);
            }
        });

        // Выход из программы
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private boolean isNumberMessage() {
        return numberCheckBox.isSelected();
    }

    private String getDecodeMessage() {
        return decodeMessagetextArea.getText();
    }

    private String getEncodeMessage() {
        return encodeMessagetextArea.getText();
    }

    private int getN() {
        return Integer.parseInt(nTextField.getText());
    }

    private int getPublicKey() {
        return Integer.parseInt(publicKeyTextField.getText());
    }

    private int getPrivateKey() {
        return Integer.parseInt(privateKeyTextField.getText());
    }


    public static void main(String[] args) {
        new TestForm();
    }
}
