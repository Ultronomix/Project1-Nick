package com.github.Nick;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.Nick.common.datasource.ConnectionFactory;
import com.github.Nick.common.util.AppContext;
import com.github.Nick.users.User;
import com.github.Nick.users.UserDAO;

public class Reimburstment {
    public static void main(String[] args) {
        System.out.println("Start");

        UserDAO userDAO = new UserDAO();
        User loggedIn = userDAO.findUserNameByName("nick")
                                .orElseThrow(() -> new RuntimeException("No user found"));
        
        System.out.println("Loggin in user: " + loggedIn);
        
        // try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
        //     System.out.println("Connected");
        // } catch (SQLException e) {
        //     System.err.println("Could not connect");
        //     e.printStackTrace();
        // }
    }
}