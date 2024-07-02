package server;

import download.Downloader;
import download.FileReceiver;
import model.Constants;
import model.UnusedUDPPortGenerator;
import model.User;
import requests.FileUploadRequest;
import requests.LoginRequest;
import requests.PingRequest;
import responses.FileUploadAcceptedResponse;
import responses.PingResponse;
import responses.ServerErrorDisplay;
import requests.SignUpRequest;
import responses.SuccessfulLoginResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
                    }


                    else if (inputObject instanceof SignUpRequest) {
                        SignUpRequest req = (SignUpRequest) inputObject;
                        if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                            outputStream.writeObject(new ServerErrorDisplay("Username Exists"));
                            outputStream.flush();
                        } else {
                            User user = new User(req.getUsername(), req.getPassword());
                            UserHandler.getInstance().createNewUser(user);
                            outputStream.writeObject(new SuccessfulLoginResponse("Signed up successfully"));
                            outputStream.flush();
                        }


                    } else if (inputObject instanceof LoginRequest) {
                        LoginRequest req = (LoginRequest) inputObject;
                        if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                            if (UserAuthentication.getInstance().verifyLogin(req)) {
                                outputStream.writeObject(new SuccessfulLoginResponse("Logged in successfully"));
                                outputStream.flush();
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
                        new Downloader(req, ports);
                        outputStream.writeObject(new FileUploadAcceptedResponse("Suc", ports));
                        outputStream.flush();
                    } else {

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
