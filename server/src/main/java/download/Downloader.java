package download;

import UDPUtils.UDPacketCreator;
import model.Constants;
import model.UnusedUDPPortGenerator;
import requests.FileUploadRequest;

public class Downloader {
    private int numThreads;
    private int finishedThreads;
    private FileUploadRequest request;
    int[] ports;
    public Downloader(FileUploadRequest request, int[] ports) {
        this.numThreads = request.getThreadCount();
        this.finishedThreads = 0;
        this.ports = ports;
        startDownloadThreads();
    }

    private void startDownloadThreads() {
        for (int i = 0; i < numThreads; i++) {
            new Thread(new FileReceiver(
                    this,
                    Constants.getFileDirectory(request.getUsername(), request.getFileName()),
                    "localhost",
                    ports[i] + 10000,
                    ports[i])).start();
        }
    }

    public synchronized void threadFinished() {
        finishedThreads++;
        if (finishedThreads == numThreads) {
            new Thread(new FileConcatenator(
                    numThreads,
                    Constants.getFileDirectory(request.getUsername(), request.getFileName()),
                    request.getFileName())).start();
        }
    }


}
