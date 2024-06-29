package server;

import requests.FileUploadRequest;
import requests.LoginRequest;
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
                ObjectOutputStream outputStream;
                if (inputObject instanceof SignUpRequest) {
                    //todo: do what you gotta do
                } else if (inputObject instanceof LoginRequest) {
                    //todo: do what you gotta do
                } else if (inputObject instanceof FileUploadRequest) {
                    //todo: do what you gotta do
                } else if (true) {

                }
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new Object());


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
