package view.userpanel;

import backend.controller.RequestFlowController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class HomePanel extends JPanel {
    private JButton showFilesOnServerButton;
    private JButton uploadFileButton;
    private JButton searchButton;
    private JButton fileAccessRequestButton;
    private JButton showProfileButton;
    private JButton logoutButton;
    private JLabel messageDisplay;

    private static HomePanel instance;

    private HomePanel() {
        showFilesOnServerButton = new JButton("My Files");
        uploadFileButton = new JButton("Upload");
        searchButton = new JButton("Search");
        fileAccessRequestButton = new JButton("Pending Requests");
        showProfileButton = new JButton("Profile");
        logoutButton = new JButton("Log-out");
        messageDisplay = new JLabel();
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(showFilesOnServerButton,gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(uploadFileButton, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(searchButton, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(fileAccessRequestButton, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        add(showProfileButton, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(logoutButton, gridBagConstraints);


        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        add(messageDisplay, gridBagConstraints);


        buttonActionListener();


    }

    private void buttonActionListener() {
        showFilesOnServerButton.addActionListener((ActionEvent e) -> {
            RequestFlowController.getInstance().showUserFilesRequest();
        });
        uploadFileButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                RequestFlowController.getInstance().uploadFileRequest(file);
            }
        });
        searchButton.addActionListener((ActionEvent e) -> {
            // TODO: Do what you gotta do
        });
        fileAccessRequestButton.addActionListener((ActionEvent e) -> {
            // TODO: Do what you gotta do
        });
        showProfileButton.addActionListener((ActionEvent e) -> {
            // TODO: Do what you gotta do
        });
        logoutButton.addActionListener((ActionEvent e) -> {
            RequestFlowController.getInstance().logOut();
        });

    }


    public void clearText() {
        messageDisplay.setText("");
    }


    public static HomePanel getInstance() {
        if (instance == null) {
            instance = new HomePanel();
        }
        return instance;
    }

}
