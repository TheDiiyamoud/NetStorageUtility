package backend.file;

public class UploadHandler {
    private static UploadHandler instance;


    private UploadHandler() {

    }

    public static UploadHandler getInstance() {
        if (instance == null) {
            instance = new UploadHandler();
        }
        return instance;
    }
}
