package view;


import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private static MainPanel instance;


    private MainPanel() {
        setPreferredSize(new Dimension((int) (640 * 0.7),(int) (480*0.7)));
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
