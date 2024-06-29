package backend.controller;

import backend.TCPClient;
import requests.LoginRequest;
import requests.SignUpRequest;

import java.io.IOException;

public class RequestFlowController {
    private static RequestFlowController instance;


    private RequestFlowController() {

    }

    public static RequestFlowController getInstance() {
        if (instance == null) {
            instance = new RequestFlowController();
        }
        return instance;
    }

    public void sendSignupRequest(String username, char[] password) {
        try {
            TCPClient.getInstance().sendRequest(new SignUpRequest(username, new String(password)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLoginRequest(String username, char[] password) {
        try {
            TCPClient.getInstance().sendRequest(new LoginRequest(username, new String(password)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
