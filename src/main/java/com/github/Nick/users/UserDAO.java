package com.github.Nick.users;

import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.Nick.common.datasource.ConnectionFactory;
import com.github.Nick.common.exceptions.DataSourceException;

// DAO = Data Access Object 
public class UserDAO {

    private final String baseSelect = "SELECT * " +
                                        "FROM ers_users";

    public List<User> getAllUsers() {

        String sql = baseSelect;

        List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            allUsers = mapResultSet2(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
        }
        return allUsers;
    }

    // public String deleteEntry(User user) {

    //     String sqlDelete = "DELETE FROM tasks.user_task " +
    //                     "WHERE name = ?";

    //     getAllUsers();

    //     try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

    //         PreparedStatement pstmt = conn.prepareStatement(sqlDelete);
    //         pstmt.setString(1, user.getName().trim().toUpperCase());

    //         pstmt.executeUpdate();

    //     } catch (Exception e) {
    //         System.err.println("Something went wrong when connecting to database.");
    //         e.printStackTrace();
    //     }
    //     return "Entry removed";
    // }

    // public String save(User user) {
    //     String sql = "INSERT INTO tasks.user_task(name, task) " +
    //             "VALUES(?, ?)";

    //     try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

    //         PreparedStatement pstmt = conn.prepareStatement(sql);
    //         pstmt.setString(1, user.getName().trim().toUpperCase());
    //         pstmt.setString(2, user.getTask().trim());

    //         pstmt.executeUpdate();

    //     } catch (Exception e) {
    //         System.err.println("Something went wrong when connecting to database.");
    //         e.printStackTrace();
    //     }
    //     return user.getName() + " " + user.getTask();
    // }

    // public Optional<User> findUserNameByName(String name) {

    //     String sql = baseSelect + "WHERE name = ?";

    //     try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

    //         PreparedStatement pstmt = conn.prepareStatement(sql);
    //         pstmt.setString(1, name.toUpperCase());
    //         ResultSet rs = pstmt.executeQuery();

    //         return mapResultSet(rs).stream().findFirst();

    //     } catch (SQLException e) {
    //         System.err.println("Something went wrong when connection to database.");
    //         e.printStackTrace();
    //     }
    //     return Optional.empty();
    // }

    private final String ers_users = "SELECT * FROM ers_users eu";

    public Optional<User> findUserNameByNameErs(String username, String password) {

        String sql = ers_users + " WHERE eu.username = ? AND eu.password = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet2(rs).stream().findFirst();

        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
    }

    public String testLog() {
        
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            System.out.println("Conneted");
            log("WORK", "Connected");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log("ERROR", e.getMessage());
            System.out.println("Logged");
        }
        return "Done";
    }

    private List<User> mapResultSet2(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setUser_id(rs.getString("user_id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setGiven_name(rs.getString("given_name"));
            user.setSurname(rs.getString("surname"));
            user.setIs_active(rs.getBoolean("is_active"));
            user.setRole_id(rs.getString("role_id"));
            users.add(user);
        }
        return users;
    }

    public void log(String level, String message) {
        try {
            File logFile = new File("src/main/resources/app.log");
            logFile.createNewFile();
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true));
            logWriter.write(String.format("\n[%s] at %s logged [%s] %s", Thread.currentThread().getName(), LocalDate.now(), level.toUpperCase(), message));
            logWriter.flush();
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
    }
}

/*  DAO that gets all users from the database, delete a user
 * from the database, or saves a user to the database.
 */