package responses;

import requests.Request;

public class DownloadFileAcceptedResponse extends ServerResponse{


    private final int threadCount;
    private final String fileName;
    private final String username;

    public DownloadFileAcceptedResponse(String message, String username, String fileName, int threadCount) {
        super(message);
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


