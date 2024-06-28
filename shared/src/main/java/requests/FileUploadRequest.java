package requests;

public class FileUploadRequest extends Request{
    private String fileID;
    private int threadCount;
    private String fileName;

    public FileUploadRequest(String fileID, int threadCount, String fileName) {
        this.fileID = fileID;
        this.threadCount = threadCount;
        this.fileName = fileName;
    }

    public String getFileID() {
        return fileID;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public String getFileName() {
        return fileName;
    }




}
