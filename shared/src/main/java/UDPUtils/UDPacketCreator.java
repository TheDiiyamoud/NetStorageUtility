package UDPUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPacketCreator {
    private static UDPacketCreator instance;
    private ByteChecksum byteChecksum = new ByteChecksum();
    private UDPacketCreator() {

    }

    public static UDPacketCreator getInstance() {
        if (instance == null) {
            instance = new UDPacketCreator();
        }
        return instance;
    }

    public DatagramPacket getSequencedPacket(byte[] buffer, int sequenceNumber, InetAddress ipAddress, int port) {
        byte[] newBuffer = new byte[buffer.length + 4];

        // Copying the contents of data buffer to the 4 -> 1027 slots in the new array
        System.arraycopy(buffer, 0, newBuffer, 4, buffer.length);
        /*
         * Java integers are 32 bit, and here we use byte operators
         * to add 8 bits of them in each array slot, by shifting 8 bits
         * rightwise each time. This would make it so that newBuffer[0] has
         * the 8 most significant digits, and newBuffer[1] has the next 8
         * bits of digits, and so on. Later, we would reverse the shifting
         * and add them up, obtaining the correct order of packets upon receiving.
         */
        newBuffer[0] = (byte) (sequenceNumber >> 24);
        newBuffer[1] = (byte) (sequenceNumber >> 16);
        newBuffer[2] = (byte) (sequenceNumber >> 8);
        newBuffer[3] = (byte) (sequenceNumber);
        byte[] hashIncludedBuffer = byteChecksum.getHashIncludedDataArray(newBuffer);
        return new DatagramPacket(hashIncludedBuffer, hashIncludedBuffer.length, ipAddress, port);
    }
}
