package download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileConcatenator implements Runnable{

    private final int numChunks;
    private final String fileSaveAddress;
    private final String fileName;
    public FileConcatenator(int numChunks, String filePartsAddress, String fileName,String fileSaveAddress) {
        this.numChunks = numChunks;
        this.fileSaveAddress = fileSaveAddress;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        String[] chunkFileNames = new String[numChunks];


        for (int i = 0; i < numChunks; i++) {
            chunkFileNames[i] = fileName + (i + 1) + ".dat";
            chunkFileNames[i] = fileSaveAddress + File.separator + chunkFileNames[i];
        }

        try (FileOutputStream fos = new FileOutputStream(fileSaveAddress)) {
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

        System.out.println("Chunks have been concatenated into: " + fileSaveAddress);
    }

}

