package server;

import model.Constants;
import requests.*;
import responses.*;
import udp.download.Downloader;
import udp.UDPUtils.UnusedUDPPortGenerator;
import model.User;
import udp.upload.FileDecomposer;
import udp.upload.Uploader;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.username = "";
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                if (!socket.isClosed()) {
                    Object inputObject = inputStream.readObject();
                    if (inputObject instanceof PingRequest) {
                        outputStream.writeObject(new PingResponse("Ping"));
                        outputStream.flush();
                    } else if (inputObject instanceof SignUpRequest) {
                        SignUpRequest req = (SignUpRequest) inputObject;
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


                    } else if (inputObject instanceof LoginRequest) {
                        LoginRequest req = (LoginRequest) inputObject;
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


                    } else if (inputObject instanceof FileUploadRequest) {
                        FileUploadRequest req = (FileUploadRequest) inputObject;
                        int[] ports = UnusedUDPPortGenerator.getPorts(req.getThreadCount());


                        new Downloader(
                                req.getFileName(),
                                ports,
                                Constants.getFileDirectory(req.getUsername(),
                                        req.getFileName()),
                                Constants.getHostName(),
                                req.getThreadCount());


                        outputStream.writeObject(new FileUploadAcceptedResponse("Suc", ports));
                        outputStream.flush();

                    } else if (inputObject instanceof ShowUserFilesRequest) {
                        ShowUserFilesRequest req = (ShowUserFilesRequest) inputObject;
                        ArrayList<String> fileNames = Constants.getUserFiles(username);
                        if (fileNames != null) {
                            outputStream.writeObject(new ShowFilesAcceptedResponse("Suc", fileNames));
                        } else {
                            outputStream.writeObject(new ServerErrorDisplay("No Files"));
                        }
                        outputStream.flush();
                    } else if (inputObject instanceof FileRemovingRequest){
                        FileRemovingRequest frr = (FileRemovingRequest) inputObject;
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

                    } else if (inputObject instanceof GetFileInfoRequest) {
                        GetFileInfoRequest req = (GetFileInfoRequest) inputObject;
                        File fileToDownload = new File(Constants.getFileDirectory(username, req.getFileName()) + File.separator + req.getFileName());
                        int threadCount = FileDecomposer.getNumChunks(fileToDownload);
                        outputStream.writeObject(new FileInfoResponse("Suc", threadCount));
                        outputStream.flush();


                    } else if (inputObject instanceof FileDownloadRequest) {
                        FileDownloadRequest r = (FileDownloadRequest) inputObject;
                        File file = new File(Constants.getFileDirectory(username, r.getFileName()) + File.separator + r.getFileName());
                        FileDecomposer decomposer = new FileDecomposer(file.getAbsolutePath());
                        new Uploader(decomposer, Constants.getHostName(), r.getPorts());
                    }

                } else {
                    inputStream.close();
                    outputStream.close();
                    System.out.println("Socket is closed");
                    break;
                }
            }
        } catch (EOFException e) {
            try {
                socket.close();
            } catch (IOException n) {
                n.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                System.out.println("Something went wrong, socket is closing...");
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

    }
}
