package backend;

import requests.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClient {
    private static TCPClient instance;
    private final Socket socket;
    private TCPClient() throws IOException {
        this.socket = new Socket("localhost", 4321);
    }


    public static TCPClient getInstance() throws IOException{
        if (instance == null) {
            instance = new TCPClient();
        }
        return instance;
    }

    public void sendRequest(Request request) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);

            //TODO: Handle proper response
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
