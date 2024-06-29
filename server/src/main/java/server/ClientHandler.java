package server;

import model.User;
import requests.FileUploadRequest;
import requests.LoginRequest;
import requests.ServerErrorDisplay;
import requests.SignUpRequest;

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
                    SignUpRequest req = (SignUpRequest) inputObject;
                    if (UserAuthentication.getInstance().usernameExists(req.getUsername())) {
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        outputStream.writeObject(new ServerErrorDisplay("Username Exists"));
                        continue;
                    }
                    User user = new User(req.getUsername(), req.getPassword());
                    UserHandler.getInstance().createNewUser(user);

                } else if (inputObject instanceof LoginRequest) {
                    //todo: do what you gotta do
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
