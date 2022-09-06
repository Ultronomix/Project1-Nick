package com.github.Nick.common.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Implement ConnectionFactory and singleton
public class ConnectionFactory {

    private static ConnectionFactory connFactory;
    private Properties dbProps = new Properties();
    

    public ConnectionFactory() {

        try {
            //Class.forName("org.postgresql.Driver");
            dbProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        }// catch (ClassNotFoundException e) {
        //     throw new RuntimeException("Failed to load PostgreSQL JDBC driver.", e);
        // } 
        catch (IOException e) {
            throw new RuntimeException("Could not read from .properties file.", e);
        }
    }

    public static ConnectionFactory getInstance() {
        if (connFactory == null) {
            connFactory = new ConnectionFactory();
        }
        return connFactory;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProps.getProperty("db_url"), dbProps.getProperty("db_username"), dbProps.getProperty("db_password"));
    }
}

/*  Creates a connection to the sql database and uses the
 * singleton design pattern to ensure that one connection is made.
 */