package view;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private static LoginPanel instance;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton proceedButton;
    private JLabel errorDisplayLabel;

    private LoginPanel() {
        this.setLayout(new GridLayout());
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        this.proceedButton = new JButton("Enter");
        this.errorDisplayLabel = new JLabel();
    }



    public static LoginPanel getInstance() {
        if (instance == null) {
            instance = new LoginPanel();
        }
        return instance;
    }
}
