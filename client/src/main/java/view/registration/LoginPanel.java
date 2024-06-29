package view.registration;

import view.MainPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private static LoginPanel instance;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton proceedButton;
    private JLabel errorDisplayLabel;
    private JButton previousMenuButton;

    private LoginPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.usernameField = new JTextField("", 20);
        this.usernameField.setBorder(new TitledBorder("Username"));
        this.passwordField = new JPasswordField("", 20);
        this.passwordField.setBorder(new TitledBorder("Password"));
        this.proceedButton = new JButton("Enter");
        this.previousMenuButton = new JButton("Back");
        this.errorDisplayLabel = new JLabel();
        this.errorDisplayLabel.setForeground(Color.red);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(usernameField, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(passwordField, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        add(proceedButton, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        add(previousMenuButton, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        add(errorDisplayLabel, gridBagConstraints);


        buttonActionListener();
    }


    void buttonActionListener() {
        proceedButton.addActionListener((ActionEvent e) -> {
            //TODO: Do what you gotta do
        });
        previousMenuButton.addActionListener((ActionEvent e) -> {
            this.resetPanel();
            MainPanel.getInstance().addComponent(RegistryPanel.getInstance());
        });
    }
    public static LoginPanel getInstance() {
        if (instance == null) {
            instance = new LoginPanel();
        }
        return instance;
    }

    public void resetPanel() {
        errorDisplayLabel.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }
}
