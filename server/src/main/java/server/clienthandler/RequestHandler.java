package server.clienthandler;

import model.Constants;
import model.User;
import requests.*;
import responses.*;
import server.UserAuthentication;
import server.UserHandler;
import udp.UDPUtils.UnusedUDPPortGenerator;
import udp.download.Downloader;
import udp.upload.FileDecomposer;
import udp.upload.Uploader;
import utils.CheckedConsumer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class RequestHandler {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Socket socket;
    private final Map<Class<?>, CheckedConsumer<Request>> requestHandlers = new HashMap<>();
    private String username;

    public RequestHandler(Socket socket) throws IOException {
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.socket = socket;
        this.username = "";
        initRequestHandlers();
    }

    public void handleRequests() throws IOException , ClassNotFoundException{
        while (true) {
            if (!socket.isClosed()) {
                Object inputObject = inputStream.readObject();
                handle(inputObject);
            } else {
                inputStream.close();
                outputStream.close();
                System.out.println("Socket is closed");
                break;
            }
        }
    }

    private void initRequestHandlers() {
        requestHandlers.put(PingRequest.class, this::handlePingRequest);
        requestHandlers.put(SignUpRequest.class, this::handleSignUpRequest);
        requestHandlers.put(LoginRequest.class, this::handleLoginRequest);
        requestHandlers.put(FileUploadRequest.class, this::handleFileUploadRequest);
        requestHandlers.put(ShowUserFilesRequest.class, this::handleShowUserFilesRequest);
        requestHandlers.put(FileRemovingRequest.class, this::handleFileRemovingRequest);
        requestHandlers.put(GetFileInfoRequest.class, this::handleGetFileInfoRequest);
        requestHandlers.put(FileDownloadRequest.class, this::handleFileDownloadRequest);

    }


    private void handle(Object request) throws IOException{
        CheckedConsumer<Request> handler = requestHandlers.get(request.getClass());
        if (handler != null) {
            handler.accept((Request) request);
        }
    }

    private void handlePingRequest(Request request) throws IOException{
        if (request instanceof PingRequest) {
            outputStream.writeObject(new PingResponse("Ping"));
            outputStream.flush();
        }
    }

    private void handleSignUpRequest(Request request) throws IOException {
        if (request instanceof SignUpRequest) {
            SignUpRequest req = (SignUpRequest) request;
            if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                outputStream.writeObject(new ServerErrorDisplay("Username Exists"));
                outputStream.flush();
            } else {
                User user = new User(req.getUsername(), req.getPassword());
                UserHandler.getInstance().createNewUser(user);
                outputStream.writeObject(new SuccessfulLoginResponse("Signed up successfully"));
                outputStream.flush();
                username = user.getUsername();
            }
        }
    }

    private void handleLoginRequest(Request request) throws IOException {
        if (request instanceof LoginRequest) {
            LoginRequest req = (LoginRequest) request;
            if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                if (UserAuthentication.getInstance().verifyLogin(req)) {
                    outputStream.writeObject(new SuccessfulLoginResponse("Logged in successfully"));
                    outputStream.flush();
                    username = req.getUsername();
                } else {
                    outputStream.writeObject(new ServerErrorDisplay("Wrong password"));
                    outputStream.flush();
                }
            } else {
                outputStream.writeObject(new ServerErrorDisplay("No such user exists"));
                outputStream.flush();
            }
        }
    }

    private void handleFileUploadRequest(Request request) throws IOException {
            if (request instanceof FileUploadRequest) {
                FileUploadRequest req = (FileUploadRequest) request;
                int[] ports = UnusedUDPPortGenerator.getPorts(req.getThreadCount());


                Downloader d = new Downloader(
                        req.getFileName(),
                        ports,
                        Constants.getFileDirectory(req.getUsername(),
                                req.getFileName()),
                        Constants.getHostName(),
                        req.getThreadCount());
                d.startDownloadThreads();

                outputStream.writeObject(new FileUploadAcceptedResponse("Suc", ports));
                outputStream.flush();
            }
    }

    private void handleShowUserFilesRequest(Request request) throws IOException{
        if (request instanceof ShowUserFilesRequest) {
            ShowUserFilesRequest req = (ShowUserFilesRequest) request;
            ArrayList<String> fileNames = Constants.getUserFiles(username);
            if (fileNames != null) {
                outputStream.writeObject(new ShowFilesAcceptedResponse("Suc", fileNames));
            } else {
                outputStream.writeObject(new ServerErrorDisplay("No Files"));
            }
            outputStream.flush();
        }

    }

    private void handleFileRemovingRequest(Request request) throws IOException{
        if (request instanceof FileRemovingRequest){
            FileRemovingRequest frr = (FileRemovingRequest) request;
            File fileToDeletePath = new File(Constants.getFileDirectory(username, frr.getFileName()));
            File fileToDelete = new File( fileToDeletePath.getAbsolutePath() + File.separator + frr.getFileName());
            if (fileToDelete.exists()) {
                if (fileToDelete.delete() && fileToDeletePath.delete()) {
                    outputStream.writeObject(new FileRemovedResponse("Suc"));
                }
            } else {
                outputStream.writeObject(new ServerErrorDisplay("Failed"));
            }
            outputStream.flush();

        }

    }

    private void handleGetFileInfoRequest(Request request) throws IOException{
        if (request instanceof GetFileInfoRequest) {
            GetFileInfoRequest req = (GetFileInfoRequest) request;
            File fileToDownload = new File(Constants.getFileDirectory(username, req.getFileName()) + File.separator + req.getFileName());
            int threadCount = FileDecomposer.getNumChunks(fileToDownload);
            outputStream.writeObject(new FileInfoResponse("Suc", threadCount, fileToDownload.length()));
            outputStream.flush();
        }
    }

    private void handleFileDownloadRequest(Request request) throws IOException{
        if (request instanceof FileDownloadRequest) {
            FileDownloadRequest r = (FileDownloadRequest) request;
            File file = new File(Constants.getFileDirectory(username, r.getFileName()) + File.separator + r.getFileName());
            FileDecomposer decomposer = new FileDecomposer(file.getAbsolutePath());
            Uploader uploader = new Uploader(decomposer, Constants.getHostName(), r.getPorts());
            uploader.beginDecomposition();
        }
    }
}

