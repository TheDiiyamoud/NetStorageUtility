package view;

import javax.swing.*;

public class InitialPanel extends JPanel{
    private static InitialPanel instance;


    private InitialPanel() {


    }

    public static InitialPanel getInstance() {
        if (instance == null) {
            instance = new InitialPanel();
        }
        return instance;
    }



}
