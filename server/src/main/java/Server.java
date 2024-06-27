import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try {
            FileReceiver receiver = new FileReceiver("127.0.0.1", 3500, 3000);
            Thread thread = new Thread(receiver);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


