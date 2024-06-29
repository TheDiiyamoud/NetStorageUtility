package server;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Constants;
import model.User;
import requests.LoginRequest;

import java.io.*;

public class UserAuthentication {
    private static UserAuthentication instance;

    private UserAuthentication() {

    }

    public static synchronized UserAuthentication getInstance() {
        if (instance == null) {
            instance = new UserAuthentication();
        }
        return instance;
    }

    public synchronized boolean usernameExists(String username) {
        File userFile = new File(Constants.getUserObjectPath(username));
        return userFile.exists();
    }


    public synchronized boolean verifyLogin(LoginRequest request) {
        try {
            String userFilePath = Constants.getUserObjectPath(request.getUsername());
            FileInputStream in = new FileInputStream(userFilePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            User user = (User) objectInputStream.readObject();
            return verifyPassword(user, request.getPassword());

        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    private synchronized boolean verifyPassword(User user, String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray());
        return result.verified;
    }




}
