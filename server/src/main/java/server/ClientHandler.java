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

public class ClientHandler implements Runnable{

    private final Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Object inputObject = inputStream.readObject();

                if (inputObject instanceof SignUpRequest) {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    SignUpRequest req = (SignUpRequest) inputObject;
                    if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                        outputStream.writeObject(new ServerErrorDisplay("Username Exists"));
                        continue;
                    }
                    User user = new User(req.getUsername(), req.getPassword());
                    UserHandler.getInstance().createNewUser(user);
                    outputStream.writeObject(new SuccessfulLoginResponse("Signed up successfully"));
                    outputStream.close();
                } else if (inputObject instanceof LoginRequest) {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    LoginRequest req = (LoginRequest) inputObject;
                    if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                        if (UserAuthentication.getInstance().verifyLogin(req)) {
                            outputStream.writeObject(new SuccessfulLoginResponse("Logged in successfully"));
                        } else {
                            outputStream.writeObject(new ServerErrorDisplay("Wrong password"));
                        }
                    } else {
                        outputStream.writeObject(new ServerErrorDisplay("No such user exists"));
                    }
                    outputStream.close();
                } else if (inputObject instanceof FileUploadRequest) {
                    //todo: do what you gotta do
                } else if (true) {

                }
                inputStream.close();


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
