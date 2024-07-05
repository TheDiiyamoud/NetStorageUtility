package responses;

public class FileInfoResponse extends ServerResponse{

    private final int threadCount;
    private final long fileSize;
    public FileInfoResponse(String message, int threadCount, long fileSize) {
        super(message);
        this.threadCount = threadCount;
        this.fileSize = fileSize;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public long getFileSize() {
        return fileSize;
    }
}
