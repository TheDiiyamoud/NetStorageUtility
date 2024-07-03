package responses;


import java.util.ArrayList;

public class ShowFilesAcceptedResponse extends ServerResponse {

    private final ArrayList<String> fileNames;
    public ShowFilesAcceptedResponse(String message, ArrayList<String> fileNames) {
        super(message);
        this.fileNames = fileNames;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }




}
