package backend.controller;

import backend.Constants;
import backend.TCPClient;
import requests.*;
import udp.UDPUtils.UnusedUDPPortGenerator;
import udp.download.Downloader;
import udp.upload.FileDecomposer;
import udp.upload.Uploader;
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
                Uploader uploader = new Uploader(decomposer, Constants.getHostName(), response.getPorts());
                uploader.setProgressBar();
                uploader.beginDecomposition();
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
                UserFilesPanel.getInstance().addFiles(response.getFileNames());
                MainPanel.getInstance().addComponent(UserFilesPanel.getInstance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDeleteFileRequest(String fileName) {
        try {
            ServerResponse r= TCPClient.getInstance().sendRequest(new FileRemovingRequest(fileName));
            if (r instanceof FileRemovedResponse) {
                showUserFilesRequest();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendDownloadFileRequest(String fileName) {
        try {
            FileInfoResponse response = sendGetFileInfoRequest(fileName);
            assert response != null;
            int[] ports = UnusedUDPPortGenerator.getPorts(response.getThreadCount());
            Downloader d = new Downloader(fileName, ports, Constants.getHostName(), response.getThreadCount(), response.getFileSize());
            d.setProgressBar();
            d.startDownloadThreads();
            TCPClient.getInstance().sendRequest(new FileDownloadRequest(fileName, ports));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileInfoResponse sendGetFileInfoRequest(String fileName) throws IOException {
        ServerResponse r = TCPClient.getInstance().sendRequest(new GetFileInfoRequest(fileName));
        if (r instanceof FileInfoResponse) {
            return (FileInfoResponse) r;
        }
        return null;
    }
}
