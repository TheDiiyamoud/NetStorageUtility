package requests;

public class FileRemovingRequest extends Request {
    private String fileName;
    public FileRemovingRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
