import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
//        try {
//            FileSender fileSender = new FileSender(
//                    "/home/dii/Desktop/Origin/filet.zip",
//                    3500,
//                    3000,
//                    "127.0.0.1");
//            Thread thread = new Thread(fileSender);
//            thread.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String filePath = "/home/dii/Desktop/Destination/filet.zip";
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

        try (FileInputStream fis = new FileInputStream(filePath)) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Number of chunks created: " + numChunks);



        String outputFilePath = "/home/dii/Desktop/Dude.zip";
        String[] chunkFileNames = new String[numChunks];


        for (int i = 0; i < numChunks; i++) {
            chunkFileNames[i] = "chunk" + (i + 1) + ".dat";
            chunkFileNames[i] = "/home/dii/Desktop/Destination/" + chunkFileNames[i];
        }

        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
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

        System.out.println("Chunks have been concatenated into: " + outputFilePath);





    }
}
