import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class FileSender {

    private final byte[] buffer;
    private final byte[] packetNumberForVerification;
    private byte[] fileName;
    private final FileInputStream fileInputStream;
    private final DatagramSocket datagramSocket;
    private final InetAddress ip;
    private final int destinationPort;


    public FileSender(String filePathName, int clientPort, int destinationPort, String hostName) throws IOException {
        File file = new File(filePathName);
        buffer = new byte[1024];
        packetNumberForVerification = new byte[1024];
        fileName = null;
        fileInputStream = new FileInputStream(file);
        datagramSocket = new DatagramSocket(clientPort);
        this.destinationPort = destinationPort;
        ip = InetAddress.getByName(hostName);
        fileName = file.getName().getBytes();
    }

    public void sendFile() {
        int sequenceNumber = 0;
        try {

            datagramSocket.send(new DatagramPacket(fileName, fileName.length, ip, destinationPort));
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
                int bytesRead;
                if ((bytesRead = fileInputStream.read(buffer)) == -1) {
                    break;
                }


                DatagramPacket datagramPacket = UDPacketCreator.getInstance().getSequencedPacket(buffer, bytesRead, sequenceNumber,ip, destinationPort);
                datagramSocket.send(datagramPacket);
                sequenceNumber++;
            }
            System.out.println("TOTAL OF: " + sequenceNumber + " packets were sent");
            String eof = "END_OF_FILE";

            datagramSocket.send(new DatagramPacket(eof.getBytes(), eof.length(), ip, destinationPort));
            datagramSocket.close();
            fileInputStream.close();


        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
