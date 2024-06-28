package backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDecomposer {
    String filePath;

    public FileDecomposer(String filePath) throws IOException{
         filePath = "/home/dii/Desktop/Destination/filet.zip";
    }

    public void decomposeFile() {
        long fileSize = new File(filePath).length();
        int numChunks;

        if (fileSize <= 100 * 1024 * 1024) {
            numChunks = 5; // Divide into 5 parts for files <= 100 MB
        } else if (fileSize <= 2L * 1024 * 1024 * 1024) {
            numChunks = 10; // Divide into 10 parts for files <= 2 GB
        } else {
            numChunks = 20; // Divide into 20 parts for larger files
        }

        int chunkSize = (int) Math.ceil((double) fileSize / numChunks);

        try (
                FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            int chunkNumber = 1;

            while ((bytesRead = fis.read(buffer)) != -1) {
                String chunkFileName = "chunk" + chunkNumber + ".dat";
                chunkFileName = "/home/dii/Desktop/Destination/" + chunkFileName;
                try (FileOutputStream fos = new FileOutputStream(chunkFileName)) {
                    fos.write(buffer, 0, bytesRead);
                }
                chunkNumber++;
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        System.out.println("Number of chunks created: " + numChunks);

    }



}
