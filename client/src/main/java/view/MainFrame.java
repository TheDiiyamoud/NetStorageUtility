package view;

import backend.TCPClient;

import javax.swing.*;
import java.awt.event.WindowListener;
import java.io.IOException;

public class MainFrame extends JFrame {
    private static MainFrame mainFrameInstance;

    private MainFrame() {
        setTitle("File Hosting Client");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        add(MainPanel.getInstance());
        pack();
        setVisible(true);

    }

    @Override
    public synchronized void addWindowListener(WindowListener l) {
        super.addWindowListener(l);
        try {
            TCPClient.getInstance().killSocket();
        } catch (IOException e) {
            System.out.println("Couldn't close socket");
        } finally {
            System.exit(0);
        }
    }

    public static MainFrame getInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }
}
