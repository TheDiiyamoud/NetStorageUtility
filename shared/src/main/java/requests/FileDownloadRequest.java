package requests;

public class FileDownloadRequest extends Request{
    private final String fileName;
    private final int[] ports;
    public FileDownloadRequest(String fileName, int[] ports) {
        this.fileName = fileName;
        this.ports = ports;
    }

    public String getFileName() {
        return fileName;
    }

    public int[] getPorts() {
        return ports;
    }
}
