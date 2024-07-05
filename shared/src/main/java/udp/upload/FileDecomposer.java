package udp.upload;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDecomposer implements Runnable{
    private final String filePath;
    private final long fileSize;
    private final int numChunks;
    private final File file;
    private int[] ports;
    private final String[] chunkNames;
    private Uploader uploader;
    public FileDecomposer(String filePath) {
         this.filePath = filePath;
         file = new File(filePath);
         fileSize = new File(filePath).length();
         numChunks = calculateNumChunks();
         chunkNames = new String[numChunks];
    }

    public void setPorts(int[] ports){
        this.ports = ports;
    }

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }


    private int calculateNumChunks() {
        int count;
        if (fileSize <= 100 * 1024 * 1024) {
            count = 5; // Divide into 5 parts for files <= 100 MB
        } else if (fileSize <= 2L * 1024 * 1024 * 1024) {
            count = 10; // Divide into 10 parts for files <= 2 GB
        } else {
            count = 20; // Divide into 20 parts for larger files
        }
        return count;

    }

    private static int calculateNumChunks(File file) {
        long size = file.length();
        int count;
        if (size <= 100 * 1024 * 1024) {
            count = 5; // Divide into 5 parts for files <= 100 MB
        } else if (size <= 2L * 1024 * 1024 * 1024) {
            count = 10; // Divide into 10 parts for files <= 2 GB
        } else {
            count = 20; // Divide into 20 parts for larger files
        }
        return count;
    }

    public static int getNumChunks(File file) {
        return calculateNumChunks(file);
    }

    public int getNumChunks() {
        return numChunks;
    }

    @Override
    public void run() {

        int chunkSize = (int) Math.ceil((double) fileSize / numChunks);
        String chunkFileName = "";
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            int chunkNumber = 1;

            while ((bytesRead = fis.read(buffer)) != -1) {
                chunkFileName = file.getName() + chunkNumber + ".dat";
                chunkNames[chunkNumber - 1] = chunkFileName;
                try (FileOutputStream fos = new FileOutputStream(chunkFileName)) {
                    fos.write(buffer, 0, bytesRead);
                }
                if (chunkNumber < numChunks) {
                    new Thread(new FileSender(
                            chunkFileName,
                            ports[chunkNumber - 1] + 10000,
                            ports[chunkNumber - 1],
                            uploader.getHostName(),
                            uploader)).start();
                    chunkNumber++;
                }
            }
            new Thread(new FileSender(
                    chunkFileName,
                    ports[chunkNumber - 1] + 10000,
                    ports[chunkNumber - 1],
                    uploader.getHostName(),
                    uploader)).start();
        } catch (
                IOException e) {
            e.printStackTrace();
        }


    }

    public String[] getChunkNames() {
        return chunkNames;
    }


    public String getFileName() {
        return file.getName();
    }

    public long getFileSize() {
        return fileSize;
    }

}
