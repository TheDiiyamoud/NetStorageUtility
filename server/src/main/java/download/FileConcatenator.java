package download;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileConcatenator implements Runnable{

    private final int numChunks;
    private final String fileAddress;

    public FileConcatenator(int numChunks, String fileAddress) {
        this.numChunks = numChunks;
        this.fileAddress = fileAddress;
    }

    @Override
    public void run() {
        String[] chunkFileNames = new String[numChunks];


        for (int i = 0; i < numChunks; i++) {
            chunkFileNames[i] = "chunk" + (i + 1) + ".dat";
            chunkFileNames[i] = "/home/dii/Desktop/Destination/" + chunkFileNames[i];
        }

        try (FileOutputStream fos = new FileOutputStream(fileAddress)) {
            for (String chunkFileName : chunkFileNames) {
                try (FileInputStream fis = new FileInputStream(chunkFileName)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Chunks have been concatenated into: " + fileAddress);
    }

}

