package model;

import java.io.File;
import java.util.ArrayList;

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
        return System.getProperty("user.home") + File.separator + "FileHostingServer";
    }

    public static synchronized String getUserObjectPath(String username) {
        return getUserPath(username) + File.separator + "user.ser";
    }

    public static synchronized String getFileDirectory(String username, String filename) {
        String path = getUserPath(username) + File.separator + filename;
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return path;
    }

    public static String getHostName() {
        return "localhost";
    }

    public static ArrayList<String> getUserFiles(String username) {
        File filesDirectory = new File(getUserPath(username));
        if (filesDirectory.exists() && filesDirectory.isDirectory()) {
            File[] userFiles = filesDirectory.listFiles();
            if (userFiles != null) {
                ArrayList<String> fileNames = new ArrayList<>();
                for (File userFile: userFiles) {
                    fileNames.add(userFile.getName());
                }
                return fileNames;
            }
        }
        return null;
    }
}
