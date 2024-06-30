package backend.controller;

import backend.TCPClient;
import requests.LoginRequest;
import requests.SignUpRequest;
import responses.ServerErrorDisplay;
import responses.ServerResponse;
import responses.SuccessfulLoginResponse;
import view.MainPanel;
import view.registration.LoginPanel;
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
            if (r instanceof ServerErrorDisplay) {
                SignUpPanel.getInstance().signupFailed();
            } else if (r instanceof SuccessfulLoginResponse) {
                MainPanel.getInstance().addComponent(HomePanel.getInstance());
                SignUpPanel.getInstance().resetPanel();
            } else {
                System.out.println("RETURN OBJECT WAS NULL!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLoginRequest(String username, char[] password) {
        try {
            ServerResponse r = TCPClient.getInstance().sendRequest(new LoginRequest(username, new String(password)));
            if (r instanceof ServerErrorDisplay) {
                LoginPanel.getInstance().loginFailed();
            } else if (r instanceof SuccessfulLoginResponse) {
                MainPanel.getInstance().addComponent(HomePanel.getInstance());
                LoginPanel.getInstance().resetPanel();
            } else {
                System.out.println("RETURN OBJECT WAS NULL");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
