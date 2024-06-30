import backend.TCPClient;
import view.MainFrame;

import javax.swing.*;

public class Client {
    public static void main(String[] args) throws Exception{


        SwingUtilities.invokeLater(MainFrame::getInstance);
        TCPClient.getInstance();








    }
}
