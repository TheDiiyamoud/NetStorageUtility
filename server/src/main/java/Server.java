import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try {
            FileReceiver receiver = new FileReceiver("127.0.0.1", 3500, 3000);
            receiver.receiveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


