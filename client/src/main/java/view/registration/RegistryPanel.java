package view.registration;

import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistryPanel extends JPanel {
    private static RegistryPanel instance;
    private JButton signupButton, loginButton;
    private RegistryPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        signupButton = new JButton("Sign-up");
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = 2;
        add(signupButton, gridBagConstraints);
        signupButton.addActionListener((ActionEvent e) -> {
            //TODO: Request the controller to spawn the sign-up panel
        });
        loginButton = new JButton("Log-in");
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(loginButton, gridBagConstraints);
        loginButton.addActionListener((ActionEvent e) -> {
            //TODO: Request the controller to spawn the log-in panel
        });
    }

    public static RegistryPanel getInstance() {
        if (instance == null) {
            instance = new RegistryPanel();
        }
        return instance;
    }
}
