package udp.UDPUtils;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JFrame {
    private final JProgressBar progressBar;

    public ProgressBar(String fileName) {
        this.setTitle("Progress on " + fileName);
        this.setSize(400, 100);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        panel.add(progressBar, BorderLayout.CENTER);
        this.add(panel);


    }

    public void setValue(int val) {
        progressBar.setValue(val);
    }
    public void finished() {
        setVisible(false);
        dispose();
    }
}
