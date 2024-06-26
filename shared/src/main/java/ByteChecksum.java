import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ByteChecksum {

    private String hashingAlgorithm = "MD5";

    public ByteChecksum() {

    }

    public ByteChecksum(String hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    public byte[] getMD5Hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashingAlgorithm);
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isValidData(byte[] data, byte[] md5Sum) {
        return MessageDigest.isEqual(md5Sum, getMD5Hash(data));
    }
}
