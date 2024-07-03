package backend.file;

import responses.FileUploadAcceptedResponse;

public class Uploader {

    private final FileUploadAcceptedResponse fileUploadAcceptedResponse;
    private final FileDecomposer fileDecomposer;
    private int finishedThreads;
    private final int numThreads;
    public Uploader(FileUploadAcceptedResponse fileUploadAcceptedResponse, FileDecomposer fileDecomposer) {
        this.fileUploadAcceptedResponse = fileUploadAcceptedResponse;
        this.fileDecomposer = fileDecomposer;
        finishedThreads = 0;
        numThreads = fileDecomposer.getNumChunks();
        beginDecomposition();
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
