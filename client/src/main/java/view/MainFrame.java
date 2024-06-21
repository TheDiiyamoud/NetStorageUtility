package view;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static MainFrame mainFrameInstance;

    private MainFrame() {
        setTitle("File Hosting Client");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(MainPanel.getInstance());
        pack();
        setVisible(true);

    }

    public static MainFrame getInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }
}
