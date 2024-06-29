package server;

import model.Constants;

import java.io.File;

public class ServerLoadup {
    public static void createDirectories() {
        String serverDirectoryPath = Constants.getServerPath();
        String userDirectoryPath = Constants.getUserPath();
        File serverDirectory = new File(serverDirectoryPath);
        serverDirectory.mkdir();
        File userDirectory = new File(userDirectoryPath);
        userDirectory.mkdir();
    }
}
