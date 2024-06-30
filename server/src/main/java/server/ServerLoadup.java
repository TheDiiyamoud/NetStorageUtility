package server;

import model.Constants;

import java.io.File;

public class ServerLoadup {
    public static void createDirectories() {
        String serverDirectoryPath = Constants.getServerPath();
        File serverDirectory = new File(serverDirectoryPath);
        serverDirectory.mkdirs();
    }
}
