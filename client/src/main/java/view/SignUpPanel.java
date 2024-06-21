package view;

import javax.swing.*;

public class SignUpPanel extends JPanel {
    private static SignUpPanel instance;
    private JTextField usernameField;
    private JPasswordField passwordField, passwordConfirmField;
    private JButton proceedButton;
    private JLabel errorDisplayerLabel;


    private SignUpPanel() {
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        passwordConfirmField = new JPasswordField();
        proceedButton = new JButton("Enter");
        errorDisplayerLabel = new JLabel();

    }


    public static SignUpPanel getInstance() {
        if (instance == null) {
            instance = new SignUpPanel();
        }
        return instance;
    }

}
