package udp.upload;

import responses.FileUploadAcceptedResponse;
import udp.UDPUtils.ProgressBar;

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
        progressBar = new ProgressBar(fileDecomposer.getFileName());
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

}


