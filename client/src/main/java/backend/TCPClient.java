package backend;

import requests.PingRequest;
import requests.Request;
import responses.PingResponse;
import responses.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClient {
    private static TCPClient instance;
    private final Socket socket;
    private Request request = null;
    private ServerResponse serverResponse = null;
    private TCPClient() throws IOException {
        this.socket = new Socket("localhost", 4321);
        startClient();
    }


    public static TCPClient getInstance() throws IOException{
        if (instance == null) {
            instance = new TCPClient();
        }
        return instance;
    }

    public ServerResponse sendRequest(Request request) {
        setRequest(request);
        ServerResponse r = serverResponse;
        resetResponse();
        return r;
    }

    private void setRequest(Request request) {
        this.request = request;
    }

    private void setResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    private void resetRequest() {
        this.request = null;
    }

    private void resetResponse() {
        this.serverResponse = null;
    }

    private void startClient() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                try {
                    if (!socket.isClosed()) {
                        if (request != null) {
                            objectOutputStream.writeObject(request);
                            objectOutputStream.flush();
                            resetRequest();
                            Thread.sleep(5000);
                        } else {
                            objectOutputStream.writeObject(new PingRequest());
                            objectOutputStream.flush();
                            System.out.println("Pinging");
                        }
                        Object o = objectInputStream.readObject();
                        if (!(o instanceof PingResponse) && o != null) {
                            setResponse((ServerResponse) o);
                        }

                        Thread.sleep(1000);

                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            objectOutputStream.close();
            objectInputStream.close();


            //TODO: Handle proper response
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
