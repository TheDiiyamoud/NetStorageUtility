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
            File file = new File("/home/dii/Desktop/Destination/bible.pdf");
            fileOutputStream = new FileOutputStream(file);
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 1044);
                datagramSocket.receive(datagramPacket);
                System.out.println(getPacketNumber(datagramPacket.getData()));
                System.out.println(datagramPacket.getData().length);
                verifyPacketIntegrity(datagramPacket.getData());
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
        return (sequencedPacket[0]<<24) + (sequencedPacket[1] << 16) + (sequencedPacket[2] << 8) + sequencedPacket[3];
    }

    private static void verifyPacketIntegrity(byte[] data) {
        byte[] hash = new byte[16];
        byte[] actualData = new byte[1028];
        System.arraycopy(data, 0, actualData, 0, actualData.length);
        System.arraycopy(data, actualData.length, hash, 0, hash.length);
        byte[] calculatedHash = byteChecksum.getMD5Hash(actualData);
        System.out.println(MessageDigest.isEqual(hash, calculatedHash));

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


