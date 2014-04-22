package experiment.form;

import dataprotection.lab.three.rsa.RSA;
import scala.Tuple5;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author phpusr
 *         Date: 04.04.14
 *         Time: 11:31
 */

/**
 * Главная форма Шифратора/Дешифратора
 */
public class MainForm extends JFrame {
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

    public MainForm() {
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
                Tuple5<Object,Object,Object,Object,Object> keys = RSA.generateKeys();
                nTextField.setText(keys._3().toString());
                publicKeyTextField.setText(keys._4().toString());
                privateKeyTextField.setText(keys._5().toString());
            }
        });

        // Шифрование
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encodeMessage = null;
                if (isNumberMessage()) {
                    encodeMessage = RSA.encryptNumber(getDecodeMessage(), getN(), getPublicKey());
                } else {
                    encodeMessage = RSA.encryptString(getDecodeMessage(), getN(), getPublicKey());
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
                    decodeMessage = RSA.decryptNumber(getEncodeMessage(), getN(), getPrivateKey());
                } else {
                    decodeMessage = RSA.decryptString(getEncodeMessage(), getN(), getPrivateKey());
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
        new MainForm();
    }
}
