package responses;

import requests.Request;

public class SuccessfulLoginResponse extends Request {
    private String message;

    public SuccessfulLoginResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
