package download;

import UDPUtils.ByteChecksum;
import model.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class FileReceiver implements Runnable{

    private final ByteChecksum byteChecksum = new ByteChecksum();
    private FileOutputStream fileOutputStream = null;
    private DatagramSocket datagramSocket = null;
    private final int serverPort;
    private final int clientPort;
    private InetAddress ip;
    private String fileDirectory;
    private Downloader downloader;

    public FileReceiver(Downloader downloader,String fileDirectory, String clientAddress,  int clientPort, int serverPort) {
        this.downloader = downloader;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        try {
            ip = InetAddress.getByName(clientAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.fileDirectory = fileDirectory;
    }

    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket(serverPort);
            byte[] buffer = new byte[1044];
            byte[] fileName = new byte[65000];
            DatagramPacket fileNamePacket = new DatagramPacket(fileName, fileName.length);
            datagramSocket.receive(fileNamePacket);
            String fileNameString = new String(fileNamePacket.getData(), 0, fileNamePacket.getLength());
            File file = new File(fileDirectory + File.separator + fileNameString);

            fileOutputStream = new FileOutputStream(file);
            int sequence = 0;
            while (true) {
                datagramSocket.send(new DatagramPacket(ByteBuffer.allocate(4).putInt(sequence).array(), 4, ip, clientPort));
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 1044);
                datagramSocket.receive(datagramPacket);
                if (checkForEOF(datagramPacket)) {
                    break;
                }
                boolean isReceivedCompletely = byteChecksum.verifyPacketIntegrity(datagramPacket);
                boolean hasCorrectOrder = getPacketNumber(datagramPacket.getData()) == sequence;
                if (isReceivedCompletely && hasCorrectOrder) {
                    byte[] bufferToWrite = stripData(datagramPacket);
                    fileOutputStream.write(bufferToWrite);
                } else {
                    continue;
                }

                sequence++;
            }
            downloader.threadFinished();

        } catch(
                IOException e)

        {
            e.printStackTrace();
        } finally

        {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }

    }


    

    private int getPacketNumber(byte[] sequencedPacket) {
        return ((sequencedPacket[0] & 0xFF) << 24) + ((sequencedPacket[1] & 0xFF) << 16) + ((sequencedPacket[2] & 0xFF) << 8) + (sequencedPacket[3] & 0xFF);
    }


    private boolean checkForEOF(DatagramPacket dp) {
        String s = new String(dp.getData(), 0, dp.getLength());
        if (s.equals("END_OF_FILE")) {
            return true;
        }
        return false;
//        return s.equals("END_OF_FILE");
    }


    private byte[] stripData(DatagramPacket packet) {
        byte[] data = packet.getData();
        int dataLength = packet.getLength() - (16 + 4);
        byte[] usefulData = new byte[dataLength];
        System.arraycopy(data, 4, usefulData, 0, usefulData.length);
        return usefulData;
    }
}
