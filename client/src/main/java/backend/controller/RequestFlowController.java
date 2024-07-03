package backend.controller;

import backend.TCPClient;
import backend.file.FileDecomposer;
import backend.file.Uploader;
import requests.FileUploadRequest;
import requests.LoginRequest;
import requests.ShowUserFilesRequest;
import requests.SignUpRequest;
import responses.*;
import view.MainPanel;
import view.registration.LoginPanel;
import view.registration.RegistryPanel;
import view.registration.SignUpPanel;
import view.userpanel.HomePanel;
import view.userpanel.UserFilesPanel;

import java.io.File;
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
                            threadCount));
            if (r instanceof FileUploadAcceptedResponse) {
                FileUploadAcceptedResponse response = (FileUploadAcceptedResponse) r;
                new Uploader(response, decomposer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserFilesRequest() {
        try {
            ServerResponse r = TCPClient.getInstance().sendRequest(new ShowUserFilesRequest(TCPClient.getInstance().getCurrentUsername()));
            if (r instanceof ShowFilesAcceptedResponse) {
                ShowFilesAcceptedResponse response = (ShowFilesAcceptedResponse) r;
                UserFilesPanel.getInstance().setFileNames(response.getFileNames());
                MainPanel.getInstance().addComponent(UserFilesPanel.getInstance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
