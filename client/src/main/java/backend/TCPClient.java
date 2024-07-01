package backend;

import requests.PingRequest;
import requests.Request;
import responses.PingResponse;
import responses.ServerResponse;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPClient implements Runnable {
    private static TCPClient instance;
    private volatile Socket socket;
    private Thread thread;
    private Request request = null;
    private ServerResponse serverResponse = null;
    private volatile boolean isRunning = true;
    private String currentUsername;
    private TCPClient() throws IOException {
        this.socket = new Socket("localhost", 4321);
        thread = new Thread(this);
        thread.start();
    }


    public static TCPClient getInstance() throws IOException {
        if (instance == null) {
            instance = new TCPClient();
        }
        return instance;
    }

    public synchronized ServerResponse sendRequest(Request request) {
        setRequest(request);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (isRunning) {

                if (!socket.isClosed()) {
                    if (request != null) {
                        objectOutputStream.writeObject(request);
                        objectOutputStream.flush();
                        resetRequest();
                    } else {
                        objectOutputStream.writeObject(new PingRequest());
                        objectOutputStream.flush();
                    }

                    Object o = objectInputStream.readObject();
                    if (!(o instanceof PingResponse) && o != null) {
                        setResponse((ServerResponse) o);
                    }

                } else {
                    break;
                }

            }
            objectOutputStream.close();
            objectInputStream.close();


            //TODO: Handle proper response
        } catch (SocketException ex) {
            System.out.println("Socket closed");
        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        }
    }

    public synchronized void killSocket() {
        try {
            isRunning = false;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }


    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public void resetCurrentUsername() {
        this.currentUsername = null;
    }


}
