package view.userpanel;

import javax.swing.*;
import java.util.ArrayList;

public class ServerFilesPanel extends JPanel {
    private static ServerFilesPanel instance;
    private JLabel messageDisplay;
    private JButton deleteButton;
    private JButton downloadButton;
    private JButton manageAccess;

    private ServerFilesPanel() {

    }


    public static ServerFilesPanel getInstance() {
        if (instance == null) {
            instance = new ServerFilesPanel();
        }
        return instance;
    }





    public void getServerFiles(ArrayList<String> files) {
        //TODO: Implement
    }
}
