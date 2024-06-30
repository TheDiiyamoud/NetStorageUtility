package backend;

import requests.Request;
import responses.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    public ServerResponse sendRequest(Request request) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse response = (ServerResponse) objectInputStream.readObject();
            objectOutputStream.close();
            objectInputStream.close();
            return response;
            //TODO: Handle proper response
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
