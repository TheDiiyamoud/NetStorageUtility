package server.clienthandler;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private RequestHandler requestHandler;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            requestHandler = new RequestHandler(socket);
            requestHandler.handleRequests();
        } catch (EOFException e) {
            try {
                socket.close();
            } catch (IOException n) {
                n.printStackTrace();
            }
        } catch (IOException |ClassNotFoundException e ) {
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
