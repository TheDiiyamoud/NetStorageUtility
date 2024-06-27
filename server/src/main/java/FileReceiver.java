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
    private final InetAddress ip;

    public FileReceiver(String clientAddress,  int clientPort, int serverPort) throws UnknownHostException {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        ip = InetAddress.getByName(clientAddress);
    }

    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket(serverPort);
            byte[] buffer = new byte[1044];
            byte[] fileName = new byte[65000];
            byte[] bufferToWrite = new byte[1024];
            DatagramPacket fileNamePacket = new DatagramPacket(fileName, fileName.length);
            datagramSocket.receive(fileNamePacket);
            String fileNameString = new String(fileNamePacket.getData(), 0, fileNamePacket.getLength());
            System.out.println("the file name is " + fileNameString);
            File file = new File("/home/dii/Desktop/Destination/" + fileNameString);

            fileOutputStream = new FileOutputStream(file);
            int sequence = 0;
            while (true) {
                datagramSocket.send(new DatagramPacket(ByteBuffer.allocate(4).putInt(sequence).array(), 4, ip, clientPort));
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 1044);
                datagramSocket.receive(datagramPacket);
                boolean isReceivedCompletely = byteChecksum.verifyPacketIntegrity(datagramPacket);
                boolean hasCorrectOrder = getPacketNumber(datagramPacket.getData()) == sequence;
                if (checkForEOF(datagramPacket)) {
                    break;
                }
                if (isReceivedCompletely && hasCorrectOrder) {
                    bufferToWrite = stripData(datagramPacket);
                    fileOutputStream.write(bufferToWrite);
                } else {
                    continue;
                }

                sequence++;
            }
            System.out.println("Total of " + sequence + " packets received");

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
            System.out.println("TRUEEEE");
            return true;
        }
        return false;
//        return s.equals("END_OF_FILE");
    }


    private byte[] stripData(DatagramPacket packet) {
        byte[] data = packet.getData();
        byte[] usefulDate = new byte[1024];
        System.arraycopy(data, 4, usefulDate, 0, usefulDate.length);
        return usefulDate;
    }
}
