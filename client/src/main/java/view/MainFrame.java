package view;

import backend.TCPClient;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    TCPClient.getInstance().killSocket();
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(0);
                }
            }
        });

    }



    public static MainFrame getInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }
}
