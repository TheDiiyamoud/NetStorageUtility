package view;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitButton extends JButton {

    public ExitButton() {
        super("exit");
        addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
    }


}
