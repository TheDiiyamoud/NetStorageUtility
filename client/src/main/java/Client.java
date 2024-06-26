import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Client {
    public static void main(String[] args) {
        try {
            File file = new File("/home/dii/Desktop/Origin/vid.mp4");
            byte[] buffer = new byte[1024];
            byte[] packetNumberForVerification = new byte[1024];
            byte[] fileName = null;
            FileInputStream fileInputStream = new FileInputStream(file);
            DatagramSocket datagramSocket= new DatagramSocket(3500);
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            int bytesRead;
            int sequenceNumber = 0;
            // sending the file name to server
            fileName = file.getName().getBytes();
            datagramSocket.send(new DatagramPacket(fileName, fileName.length, ip, 3000));
            while (true) {
                // verify the sequence of packets
                DatagramPacket receivedPacket = new DatagramPacket(packetNumberForVerification, packetNumberForVerification.length);
                datagramSocket.receive(receivedPacket);
                int receivedNumber = ByteBuffer.wrap(packetNumberForVerification).getInt();
                System.out.println("I received packet number " + receivedNumber);
                if (receivedNumber != sequenceNumber) {
                    continue;
                }
                System.out.println("They were equal lol");
                if ((bytesRead = fileInputStream.read(buffer)) == -1) {
                    break;
                }


                DatagramPacket datagramPacket = UDPacketCreator.getInstance().getSequencedPacket(buffer, bytesRead, sequenceNumber,ip, 3000);
                datagramSocket.send(datagramPacket);
                sequenceNumber++;
            }
            System.out.println("TOTAL OF: " + sequenceNumber + " packets were sent");
            String eof = "END_OF_FILE";

            datagramSocket.send(new DatagramPacket(eof.getBytes(), eof.length(), ip, 3000));
            datagramSocket.close();
            fileInputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
