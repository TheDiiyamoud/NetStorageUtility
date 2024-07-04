package udp.upload;

import responses.FileUploadAcceptedResponse;

public class Uploader {

    private final FileUploadAcceptedResponse fileUploadAcceptedResponse;
    private final FileDecomposer fileDecomposer;
    private int finishedThreads;
    private final int numThreads;
    private final String hostName;
    public Uploader(FileUploadAcceptedResponse fileUploadAcceptedResponse, FileDecomposer fileDecomposer, String hostName) {
        this.fileUploadAcceptedResponse = fileUploadAcceptedResponse;
        this.fileDecomposer = fileDecomposer;
        finishedThreads = 0;
        numThreads = fileDecomposer.getNumChunks();
        this.hostName = hostName;
        beginDecomposition();
    }

    public String getHostName() {
        return hostName;
    }

    private void beginDecomposition() {
        int[] portsNumbers = fileUploadAcceptedResponse.getPorts();
        fileDecomposer.setPorts(portsNumbers);
        fileDecomposer.setUploader(this);
        new Thread(fileDecomposer).start();
    }

    public synchronized void threadFinished() {
        finishedThreads++;
        if (finishedThreads == numThreads) {
            new Thread(new RemoveUploadedChunks(fileDecomposer.getChunkNames())).start();
        }
    }
}
