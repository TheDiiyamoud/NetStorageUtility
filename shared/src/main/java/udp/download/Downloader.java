package udp.download;



public class Downloader {
    private final int threadCount;
    private int finishedThreads;
    private final String fileDirectory;
    private final String hostName;
    private final String fileName;
    int[] ports;
    public Downloader(String fileName, int[] ports, String fileDirectory, String hostName, int threadCount) {
        this.threadCount = threadCount;
        this.finishedThreads = 0;
        this.ports = ports;
        this.fileDirectory = fileDirectory;
        this.hostName = hostName;
        this.fileName = fileName;
        startDownloadThreads();
    }

    private void startDownloadThreads() {
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
        }
    }


}
