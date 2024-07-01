package backend.file;

import java.io.File;

public class ClientDirectoryHandler {

    public static String getClientDirectory() {
        String path = System.getProperty("user.home") + File.separator + "FileHostingClient";
        mkdir(path);
        return path;
    }

    public static String getUserDirectory(String username) {
        String path = getClientDirectory() + File.separator + username;
        mkdir(path);
        return path;
    }

    public static void mkdir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
    }
}
