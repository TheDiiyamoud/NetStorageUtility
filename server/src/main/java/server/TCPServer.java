package server;

import server.clienthandler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private static TCPServer instance;

    private final ServerSocket serverSocket;
    private TCPServer() throws IOException {
        this.serverSocket = new ServerSocket(4321);
    }

    public static TCPServer getInstance() throws IOException{
        if (instance == null) {
             instance = new TCPServer();
        }
        return instance;
    }

    public void start() throws IOException {
        ServerLoadup.createDirectories();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Established a connection with " + clientSocket);
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
}
