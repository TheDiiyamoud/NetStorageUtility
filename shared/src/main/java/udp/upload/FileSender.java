package udp.upload;

import udp.UDPUtils.UDPacketCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class FileSender implements Runnable{

    private final byte[] buffer;
    private final byte[] packetNumberForVerification;
    private byte[] fileName;
    private final FileInputStream fileInputStream;
    private final DatagramSocket datagramSocket;
    private final InetAddress ip;
    private final int destPort;
    private final Uploader uploader;


    public FileSender(String filePathName, int sourcePort, int destPort, String hostName, Uploader uploader) throws IOException {
        File file = new File(filePathName);
        buffer = new byte[1024];
        packetNumberForVerification = new byte[1024];
        fileName = null;
        fileInputStream = new FileInputStream(file);
        datagramSocket = new DatagramSocket(sourcePort);
        this.destPort = destPort;
        ip = InetAddress.getByName(hostName);
        fileName = file.getName().getBytes();
        this.uploader = uploader;
    }

    @Override
    public void run() {
        int sequenceNumber = 0;
        long previousBytesRead = 0;
        long currentBytesRead = previousBytesRead;
        try {

            datagramSocket.send(new DatagramPacket(fileName, fileName.length, ip, destPort));
            while (true) {
                // verify the sequence of packets
                DatagramPacket receivedPacket = new DatagramPacket(packetNumberForVerification, packetNumberForVerification.length);
                datagramSocket.receive(receivedPacket);
                int receivedNumber = ByteBuffer.wrap(packetNumberForVerification).getInt();
                if (receivedNumber != sequenceNumber) {
                    continue;
                }
                int bytesRead = fileInputStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                byte[] bufferToBeSent;
                bufferToBeSent = Arrays.copyOf(buffer, bytesRead); // Adjusting the size of final buffer.



                DatagramPacket datagramPacket = UDPacketCreator.getInstance().getSequencedPacket(bufferToBeSent, sequenceNumber,ip, destPort);
                datagramSocket.send(datagramPacket);
                if (sequenceNumber % 10 == 0) {
                    currentBytesRead = fileInputStream.getChannel().position();
                    currentBytesRead -= previousBytesRead;
                    uploader.update(currentBytesRead);
                    previousBytesRead = fileInputStream.getChannel().position();
                }
                sequenceNumber++;
            }
            String eof = "END_OF_FILE";

            datagramSocket.send(new DatagramPacket(eof.getBytes(), eof.length(), ip, destPort));
            datagramSocket.close();
            fileInputStream.close();
            uploader.threadFinished();

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
