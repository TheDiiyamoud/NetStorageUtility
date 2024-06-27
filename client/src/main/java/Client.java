import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try {
            FileSender fileSender = new FileSender(
                    "/home/dii/Desktop/Origin/filet.zip",
                    3500,
                    3000,
                    "127.0.0.1");
            fileSender.sendFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
