package backend.file;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class UnusedUDPPorts {

    private static final Object lock = new Object();
    public static int[] getUnusedUDPPorts(int numPorts) {

        List<Integer> unusedPorts = new ArrayList<>();
        synchronized (lock) {
            // Using ports 20000 -> 30000 for client
            for (int port = 20000; port < 30000 && unusedPorts.size() < numPorts; port++) {
                try (DatagramSocket socket = new DatagramSocket(port)) {
                    unusedPorts.add(port);
                } catch (Exception e) {

                }
            }
        }
        return unusedPorts.stream().mapToInt(i -> 1).toArray();
    }
}
