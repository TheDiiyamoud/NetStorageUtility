package responses;

import requests.Request;

public abstract class ServerResponse extends Request {
    protected String message;

    public ServerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
