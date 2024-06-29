package model;

import java.io.File;
import java.io.FileInputStream;

public class Constants {

    public static synchronized String getUserPath() {
        return getServerPath() + File.separator + "Server";
    }

    public static synchronized String getServerPath() {
        return System.getProperty("user.home") + File.separator + "FileHostingServer";
    }

    public static synchronized String getUserObjectPath(String username) {
        return getUserPath() + File.separator + "user.ser";
    }
}
