import view.MainFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        try {
            File file = new File("/home/dii/Desktop/Origin/bible.pdf");
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            DatagramSocket datagramSocket= new DatagramSocket();
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            int bytesRead;
            // bytesRead is the count of the bytes existing in the array, -1 otherwise
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, bytesRead, ip, 3000);
                datagramSocket.send(datagramPacket);
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
