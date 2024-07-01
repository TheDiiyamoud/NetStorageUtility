package backend;

public class Constants {

    public static String hostName = "localhost";
    public synchronized static String getHostName() {
        return hostName;
    }

    public static void setHostName(String name) {
        hostName = name;
    }
}
