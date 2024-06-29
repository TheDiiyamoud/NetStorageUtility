package requests;

public class ServerErrorDisplay extends Request{
    private String message;

    public ServerErrorDisplay(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
