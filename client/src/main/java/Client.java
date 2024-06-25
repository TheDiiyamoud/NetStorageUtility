import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
        try {
            File file = new File("/home/dii/Desktop/Origin/bible.pdf");
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            DatagramSocket datagramSocket= new DatagramSocket();
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            int bytesRead;
            int sequenceNumber = 0;
            // bytesRead is the count of the bytes existing in the array, -1 otherwise
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                DatagramPacket datagramPacket = UDPacketCreator.getInstance().getSequencedPacket(buffer, bytesRead, sequenceNumber,ip, 3000);
                datagramSocket.send(datagramPacket);
                sequenceNumber++;
            }
            String eof = "END_OF_FILE";

            datagramSocket.send(new DatagramPacket(eof.getBytes(), eof.length(), ip, 3000));
            datagramSocket.close();
            fileInputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
