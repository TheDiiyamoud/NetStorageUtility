package udp.download;


import udp.UDPUtils.ProgressBar;

import java.io.File;
import java.nio.file.Paths;

public class Downloader {
    private final int threadCount;
    private int finishedThreads;
    private final String fileDirectory;
    private final String hostName;
    private final String fileName;
    private long fileSize;
    private long progress;
    private ProgressBar progressBar;
    int[] ports;
    public Downloader(String fileName, int[] ports, String fileDirectory, String hostName, int threadCount) {
        this.threadCount = threadCount;
        this.finishedThreads = 0;
        this.ports = ports;
        this.fileDirectory = fileDirectory;
        this.hostName = hostName;
        this.fileName = fileName;
        progressBar = new ProgressBar(fileName);
    }

    public Downloader(String fileName, int[] ports, String hostName, int threadCount, long fileSize) {
        this.threadCount = threadCount;
        this.finishedThreads = 0;
        this.ports = ports;
        this.fileDirectory = System.getProperty("user.home");
        this.hostName = hostName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        progressBar = new ProgressBar(fileName);
    }

    public void startDownloadThreads() {
        for (int i = 0; i < threadCount; i++) {
            new Thread(new FileReceiver(
                    this,
                    fileDirectory,
                    hostName,
                    ports[i] + 10000,
                    ports[i])).start();
        }
    }

    public synchronized void threadFinished() {
        finishedThreads++;
        if (finishedThreads == threadCount) {
            new Thread(new FileConcatenator(
                    threadCount,
                    fileDirectory,
                    fileName)).start();
            progressBar.finished();
        }
    }

    public void setProgressBar() {
        progressBar.setVisible(true);
    }

    public void update(long increment) {
        if (progressBar.isVisible()) {
            progress += increment;
            double percentage = ((double) progress / fileSize) * 100;
            long roundedPercentage = Math.round(percentage);
            progressBar.setValue((int) roundedPercentage);
        }
    }


}
