package view;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static MainFrame mainFrameInstance;

    private MainFrame() {
        setTitle("Generic Title");
        setLocationRelativeTo(null);
        setSize(640,480);
        setResizable(false);
        setVisible(true);

    }

    public static MainFrame getInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }
}
