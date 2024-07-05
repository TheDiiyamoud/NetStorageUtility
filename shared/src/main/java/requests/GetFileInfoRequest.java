package requests;

public class GetFileInfoRequest extends Request{
    private String fileName;
    public GetFileInfoRequest(String fileName) {
        this.fileName =fileName;
    }
    public String getFileName() {
        return fileName;
    }
}
