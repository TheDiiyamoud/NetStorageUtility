package view;


import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private static MainPanel instance;


    private MainPanel() {
        setPreferredSize(new Dimension(640,480));
        setLayout(new BorderLayout());
    }

    public static MainPanel getInstance() {
        if (instance == null) {
            instance = new MainPanel();
        }
        return instance;
    }

    public void clearPanel() {
        removeAll();
        revalidate();
        repaint();
    }

}
