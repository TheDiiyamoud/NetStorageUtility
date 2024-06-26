import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ByteChecksum {


    public byte[] getMD5Hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isValidData(byte[] data, byte[] md5Sum) {
        return MessageDigest.isEqual(md5Sum, getMD5Hash(data));
    }

    public byte[] getHashIncludedDataArray(byte[] data) {
        byte[] hashIncludedByte = new byte[data.length + 16];
        byte[] md5Sum = getMD5Hash(data);
        System.arraycopy(data, 0, hashIncludedByte, 0, data.length);
        System.arraycopy(md5Sum, 0, hashIncludedByte, data.length, md5Sum.length);
        return hashIncludedByte;
    }
}
