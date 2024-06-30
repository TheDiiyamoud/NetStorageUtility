package server;

import model.User;
import requests.FileUploadRequest;
import requests.LoginRequest;
import responses.ServerErrorDisplay;
import requests.SignUpRequest;
import responses.SuccessfulLoginResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                if (!socket.isClosed()) {
                    Object inputObject = inputStream.readObject();
                    if (inputObject instanceof SignUpRequest) {

                        SignUpRequest req = (SignUpRequest) inputObject;
                        if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                            outputStream.writeObject(new ServerErrorDisplay("Username Exists"));
                            outputStream.flush();
                            continue;
                        }
                        User user = new User(req.getUsername(), req.getPassword());
                        UserHandler.getInstance().createNewUser(user);
                        outputStream.writeObject(new SuccessfulLoginResponse("Signed up successfully"));
                        outputStream.flush();
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
                        //todo: do what you gotta do
                    } else if (true) {

                    }
                    inputStream.close();
                    outputStream.close();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

    }
}
