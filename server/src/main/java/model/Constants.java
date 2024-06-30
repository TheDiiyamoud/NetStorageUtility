package model;

import java.io.File;
import java.io.FileInputStream;

public class Constants {

    public static synchronized String getUserPath(String username) {
        String path = getServerPath() + File.separator + username;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return path;
    }

    public static synchronized String getServerPath() {
        return System.getProperty("user.home") + File.separator + "FileHostingServer" + File.separator + "server";
    }

    public static synchronized String getUserObjectPath(String username) {
        return getUserPath(username) + File.separator + "user.ser";
    }
}
