package view.userpanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserFilesPanel extends JPanel {
    private static UserFilesPanel instance;
    private JLabel messageDisplay;
    private JButton deleteButton;
    private JButton downloadButton;
    private JButton manageAccess;
    private ArrayList<String> fileNames;
    //TODO: Complete this panel
    private UserFilesPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        messageDisplay = new JLabel();
        deleteButton = new JButton("Delete File");
        downloadButton = new JButton("Download File");
        manageAccess = new JButton("Manage File Access");


    }


    public static UserFilesPanel getInstance() {
        if (instance == null) {
            instance = new UserFilesPanel();
        }
        return instance;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }





    public void getServerFiles(ArrayList<String> files) {
        //TODO: Implement
    }
}
