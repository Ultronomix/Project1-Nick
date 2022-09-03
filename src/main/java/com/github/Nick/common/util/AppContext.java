package com.github.Nick.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.github.Nick.users.UserDAO;

public class AppContext {
    private static boolean appRunning;

    public AppContext() {
        appRunning = true;
    }

    public void startApp() {
        while (appRunning) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.getAllUsers().forEach(System.out::println);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void shutdown() {
        appRunning = false;
    }

}
