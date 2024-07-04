package udp.upload;

import responses.FileUploadAcceptedResponse;

public class Uploader {


    private final FileDecomposer fileDecomposer;
    private int finishedThreads;
    private final int numThreads;
    private final String hostName;
    private final int[] portsNumbers;
    public Uploader(FileDecomposer fileDecomposer, String hostName, int[] downloaderPorts) {

        this.fileDecomposer = fileDecomposer;
        finishedThreads = 0;
        numThreads = fileDecomposer.getNumChunks();
        this.hostName = hostName;
        portsNumbers = downloaderPorts;
        beginDecomposition();
    }

    public String getHostName() {
        return hostName;
    }

    private void beginDecomposition() {
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
