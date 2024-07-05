package view.userpanel;

import backend.controller.RequestFlowController;
import view.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class UserFilesPanel extends JPanel {
    private static UserFilesPanel instance;
    private JButton deleteButton;
    private JButton downloadButton;
    private JButton manageAccess;
    private JButton previousMenuButton;
    private ArrayList<String> fileNames;
    private JList<String> displayFiles;
    private DefaultListModel<String> listModel;
    private JPanel buttonsPanel;


    private UserFilesPanel() {
        this.setLayout(new BorderLayout());
        constructButtons();
        listModel = new DefaultListModel<>();
        displayFiles = new JList<>(listModel);
        this.add(displayFiles, BorderLayout.NORTH);
        displayFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        manageButtons();


    }


    public static UserFilesPanel getInstance() {
        if (instance == null) {
            instance = new UserFilesPanel();
        }
        return instance;
    }

    public void addFiles(ArrayList<String> fileNames) {
        listModel.clear();
        for (String fileName: fileNames) {
            if (fileName.equals("user.ser")) {
                continue;
            }
            listModel.addElement(fileName);
        }
    }

    private void constructButtons() {
        buttonsPanel = new JPanel();
        deleteButton = new JButton("Delete File");
        downloadButton = new JButton("Download the selected file");
        manageAccess = new JButton("Manage Access"); //Probably not going to be implemented
        previousMenuButton = new JButton("Back");
        buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonsPanel.add(downloadButton, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonsPanel.add(deleteButton, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonsPanel.add(manageAccess, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonsPanel.add(previousMenuButton, gbc);

        this.add(buttonsPanel, BorderLayout.CENTER);

    }

    private void manageButtons() {


        deleteButton.addActionListener((ActionEvent e) -> {
            String selected = displayFiles.getSelectedValue();
            if (selected != null) {
                RequestFlowController.getInstance().sendDeleteFileRequest(selected);
            }

        });


        downloadButton.addActionListener((ActionEvent e)-> {
            String selected = displayFiles.getSelectedValue();
            if (selected != null) {
                RequestFlowController.getInstance().sendDownloadFileRequest(selected);
            }
        });


        previousMenuButton.addActionListener((ActionEvent e) -> {
            MainPanel.getInstance().addComponent(HomePanel.getInstance());
        });


        manageAccess.addActionListener((ActionEvent e) -> {
            System.out.println("NOT IMPLEMENTED");
        });

    }
}
