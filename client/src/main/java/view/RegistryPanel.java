package view;

import javax.swing.*;

public class RegistryPanel extends JPanel {
    private static RegistryPanel instance;

    private RegistryPanel() {

    }

    public static RegistryPanel getInstance() {
        if (instance == null) {
            instance = new RegistryPanel();
        }
        return instance;
    }
}
