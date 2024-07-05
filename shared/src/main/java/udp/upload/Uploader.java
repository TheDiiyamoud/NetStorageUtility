package udp.upload;

import responses.FileUploadAcceptedResponse;

import javax.swing.*;
import java.awt.*;

public class Uploader {


    private final FileDecomposer fileDecomposer;
    private final long fileSize;
    private int finishedThreads;
    private volatile long progress;
    private final int numThreads;
    private final String hostName;
    private final int[] portsNumbers;
    private final ProgressBar progressBar;
    public Uploader(FileDecomposer fileDecomposer, String hostName, int[] downloaderPorts) {

        this.fileDecomposer = fileDecomposer;
        fileSize = fileDecomposer.getFileSize();
        progress = 0L;
        finishedThreads = 0;
        numThreads = fileDecomposer.getNumChunks();
        this.hostName = hostName;
        portsNumbers = downloaderPorts;
        progressBar = new ProgressBar();
    }

    public String getHostName() {
        return hostName;
    }

    public void beginDecomposition() {
        fileDecomposer.setPorts(portsNumbers);
        fileDecomposer.setUploader(this);
        new Thread(fileDecomposer).start();
    }

    public void setProgressBar() {
        progressBar.setVisible(true);
    }

    public synchronized void threadFinished() {
        finishedThreads++;
        if (finishedThreads == numThreads) {
            new Thread(new RemoveUploadedChunks(fileDecomposer.getChunkNames())).start();
            progressBar.finished();
        }
    }

    public synchronized void update(long increment) {
        if (progressBar.isVisible()) {
            progress += increment;
            double percentage = ((double) progress / fileSize) * 100;
            long roundedPercentage = Math.round(percentage);
            progressBar.setValue((int) roundedPercentage);
        }
    }

    private class ProgressBar extends JFrame {
        private final JProgressBar progressBar;

        ProgressBar() {
            this.setTitle("Progress on " + fileDecomposer.getFileName());
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
}


