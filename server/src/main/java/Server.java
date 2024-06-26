import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.MessageDigest;

public class Server {
    static ByteChecksum byteChecksum = new ByteChecksum();
    public static void main(String[] args) {
        FileOutputStream fileOutputStream = null;

        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(3000);
            byte[] buffer = new byte[1044];
            File file = new File("/home/dii/Desktop/Destination/filet.zip");
            fileOutputStream = new FileOutputStream(file);
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 1044);
                datagramSocket.receive(datagramPacket);
                System.out.println(getPacketNumber(datagramPacket.getData()));
                System.out.println(datagramPacket.getData().length);
                boolean isReceivedCompletly = byteChecksum.verifyPacketIntegrity(datagramPacket);
                if (isReceivedCompletly) {
                    System.out.println("YES");
                }
                else {
                    System.out.println("NO");
                }
                if (checkForEOF(datagramPacket)) {
                    break;
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

    private static int getPacketNumber(byte[] sequencedPacket) {
        return ((sequencedPacket[0] & 0xFF)<<24) + ((sequencedPacket[1] & 0xFF)<< 16) + ((sequencedPacket[2] & 0xFF)<< 8) + (sequencedPacket[3]& 0xFF);
    }


    private static boolean checkForEOF(DatagramPacket dp) {
        String s = new String(dp.getData(), 0, dp.getLength());
        if (s.equals("END_OF_FILE")) {
            System.out.println("TRUEEEE");
            return true;
        }
        return false;
//        return s.equals("END_OF_FILE");
    }
}


