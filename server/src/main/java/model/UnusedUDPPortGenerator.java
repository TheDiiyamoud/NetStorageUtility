package model;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UnusedUDPPortGenerator {

    public static int[] getPorts(int count) {
        List<Integer> emptyPortsList = new ArrayList<>();

        for (int port = 10000; port < 20000 & emptyPortsList.size() <= count; port++) {
            try {
                DatagramSocket socket = new DatagramSocket(port);
                socket.close();
                emptyPortsList.add(port);
            } catch (SocketException e) {
                // Port is already in use
            }
        }
        return emptyPortsList.stream().mapToInt(i -> i).toArray();
    }

}
