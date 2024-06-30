package backend.controller;

import backend.TCPClient;
import requests.LoginRequest;
import requests.SignUpRequest;
import responses.ServerErrorDisplay;
import responses.ServerResponse;
import responses.SuccessfulLoginResponse;
import view.MainPanel;
import view.registration.SignUpPanel;
import view.userpanel.HomePanel;

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
            ServerResponse r = TCPClient.getInstance().sendRequest(new SignUpRequest(username, new String(password)));
            if (r instanceof ServerErrorDisplay | r == null) {
                SignUpPanel.getInstance().signupFailed();
                System.out.println("SIGN UP FAILED. RETURN OBJECT: " + r);
            } else if (r instanceof SuccessfulLoginResponse) {
                MainPanel.getInstance().addComponent(HomePanel.getInstance());
                System.out.println("LOGGED IN SUCCESSFULLY!");
            }

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
