package view;


import view.registration.RegistryPanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private static MainPanel instance;


    private MainPanel() {
        setPreferredSize(new Dimension((int) (640 * 0.7),(int) (480*0.7)));
        setLayout(new BorderLayout());
        add(RegistryPanel.getInstance(), BorderLayout.CENTER);
    }

    public static MainPanel getInstance() {
        if (instance == null) {
            instance = new MainPanel();
        }
        return instance;
    }

    private void clearPanel() {
        removeAll();
        revalidate();
        repaint();
    }

    public void addComponent(Component component) {
        this.clearPanel();
        this.add(component, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

}
