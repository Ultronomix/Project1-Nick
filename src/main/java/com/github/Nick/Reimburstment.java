package com.github.Nick;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.Nick.common.datasource.ConnectionFactory;

public class Reimburstment {
    public static void main(String[] args) {
        System.out.println("Start");
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println("Could not connect");
            e.printStackTrace();
        }
    }
    
}