import view.MainFrame;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::getInstance);
    }
}
