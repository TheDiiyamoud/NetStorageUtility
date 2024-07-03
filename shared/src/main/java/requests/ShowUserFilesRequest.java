package requests;

public class ShowUserFilesRequest extends Request {
    private String username;
    public ShowUserFilesRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
