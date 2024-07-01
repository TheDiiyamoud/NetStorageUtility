package requests;



public class FileUploadRequest extends Request{
    private int threadCount;
    private String fileName;
    private String username;
    private int[] unusedPorts;
    public FileUploadRequest(String username, String fileName, int threadCount, int[] unusedPorts) {
        this.threadCount = threadCount;
        this.fileName = fileName;
        this.username = username;
        this.unusedPorts = unusedPorts;
    }


    public int[] getUnusedPorts() {
        return unusedPorts;
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
