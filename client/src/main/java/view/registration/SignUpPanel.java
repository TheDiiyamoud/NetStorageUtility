package view.registration;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignUpPanel extends JPanel {
    private static SignUpPanel instance;
    private JTextField usernameField;
    private JPasswordField passwordField, passwordConfirmField;
    private JButton proceedButton;
    private JLabel signupFailureDisplay;


    private SignUpPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        usernameField = new JTextField("", 20);
        usernameField.setBorder(new TitledBorder("Username"));
        passwordField = new JPasswordField("", 20);
        passwordField.setBorder(new TitledBorder("Password"));
        passwordConfirmField = new JPasswordField("", 20);
        passwordConfirmField.setBorder(new TitledBorder("Confirm Password"));
        proceedButton = new JButton("Enter");
        signupFailureDisplay = new JLabel("");//TODO: Manage the error being displayed
        signupFailureDisplay.setForeground(Color.red);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        add(usernameField, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        add(passwordField, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        add(passwordConfirmField, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        add(proceedButton, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        add(signupFailureDisplay, gridBagConstraints);
        buttonActionListener();

    }


    private void buttonActionListener() {
        proceedButton.addActionListener((ActionEvent e)-> {
            //TODO: Do what you gotta do
        });
    }

    public static SignUpPanel getInstance() {
        if (instance == null) {
            instance = new SignUpPanel();
        }
        return instance;
    }

    public void resetText() {
        signupFailureDisplay.setText("");
    }

}
