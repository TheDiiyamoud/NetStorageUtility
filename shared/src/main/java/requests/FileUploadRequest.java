package requests;



public class FileUploadRequest extends Request{
    private int threadCount;
    private String fileName;
    private String username;
    public FileUploadRequest(String username, String fileName, int threadCount) {
        this.threadCount = threadCount;
        this.fileName = fileName;
        this.username = username;
    }


    public String getUsername() {
        return username;
    }
    public int getThreadCount() {
        return threadCount;
    }

    public String getFileName() {
        return fileName;
    }




}
