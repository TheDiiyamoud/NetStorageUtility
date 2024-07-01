package responses;

import java.util.ArrayList;
import java.util.Objects;

public class FileUploadAcceptedResponse extends ServerResponse{

    private final int[] ports;
    private String message;

    public FileUploadAcceptedResponse(String message, int[] ports) {
        super(message);
        this.ports = ports;
    }

    public int[] getPorts() {
        return ports;
    }
}
