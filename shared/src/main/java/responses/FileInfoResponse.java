package responses;

public class FileInfoResponse extends ServerResponse{

    private final int threadCount;

    public FileInfoResponse(String message, int threadCount) {
        super(message);
        this.threadCount = threadCount;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
