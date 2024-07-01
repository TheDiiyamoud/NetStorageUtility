package backend.file;

import backend.Constants;
import backend.TCPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileDecomposer implements Runnable{
    private final String filePath;
    private final long fileSize;
    private final int numChunks;
    private final File file;
    private int[] ports;
    public FileDecomposer(String filePath) {
         this.filePath = filePath;
         file = new File(filePath);
         fileSize = new File(filePath).length();
         numChunks = calculateNumChunks();
    }

    public void setPorts(int[] ports){
        this.ports = ports;
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
                chunkFileName = ClientDirectoryHandler.getUserDirectory(TCPClient.getInstance().getCurrentUsername()) + chunkFileName;
                try (FileOutputStream fos = new FileOutputStream(chunkFileName)) {
                    fos.write(buffer, 0, bytesRead);
                }
                if (chunkNumber < numChunks) {
                    new Thread(new FileSender(
                            chunkFileName,
                            ports[chunkNumber - 1] + 10000,
                            ports[chunkNumber - 1],
                            Constants.getHostName()));
                    chunkNumber++;
                }
            }
            new Thread(new FileSender(
                    chunkFileName,
                    ports[chunkNumber - 1] + 10000,
                    ports[chunkNumber - 1],
                    Constants.getHostName()
            ));
        } catch (
                IOException e) {
            e.printStackTrace();
        }


    }



}
