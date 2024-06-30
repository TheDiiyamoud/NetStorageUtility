package server;


import model.Constants;
import model.User;

import java.io.*;

public class UserHandler {
    private static UserHandler instance;

    private UserHandler() {

    }

    public static synchronized UserHandler getInstance() {
        if (instance == null) {
            instance = new UserHandler();
        }
        return instance;
    }

    public void createNewUser(User user) {
        try {
            String userPath = Constants.getUserObjectPath(user.getUsername());
            FileOutputStream outputStream = new FileOutputStream(userPath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            outputStream.close();
            objectOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
