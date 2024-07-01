package backend.controller;

import backend.TCPClient;
import backend.file.FileDecomposer;
import backend.file.UnusedUDPPorts;
import requests.FileUploadRequest;
import requests.LoginRequest;
import requests.SignUpRequest;
import responses.FileUploadAcceptedResponse;
import responses.ServerErrorDisplay;
import responses.ServerResponse;
import responses.SuccessfulLoginResponse;
import view.MainPanel;
import view.registration.LoginPanel;
import view.registration.RegistryPanel;
import view.registration.SignUpPanel;
import view.userpanel.HomePanel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
                TCPClient.getInstance().setCurrentUsername(username);
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
                TCPClient.getInstance().setCurrentUsername(username);
            } else {
                System.out.println("RETURN OBJECT WAS NULL");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        try {
            TCPClient.getInstance().resetCurrentUsername();
            MainPanel.getInstance().addComponent(RegistryPanel.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileRequest(File file) {
        FileDecomposer decomposer = new FileDecomposer(file.getAbsolutePath());
        int threadCount = decomposer.getNumChunks();
        try {
            ServerResponse r = TCPClient.getInstance().sendRequest(
                    new FileUploadRequest(TCPClient.getInstance().getCurrentUsername(),
                            file.getName(),
                            threadCount,
                            UnusedUDPPorts.getUnusedUDPPorts(threadCount)));
            if (r instanceof FileUploadAcceptedResponse) {
                FileUploadAcceptedResponse res = (FileUploadAcceptedResponse) r;
                int[] portsNumbers = res.getPorts();
                decomposer.setPorts(portsNumbers);
                new Thread(decomposer).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
