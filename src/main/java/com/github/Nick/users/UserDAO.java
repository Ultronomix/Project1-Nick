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

// DAO = Data Access Object 
public class UserDAO {

    private final String baseSelect = "SELECT ROW_NUMBER() OVER() AS row_num, " +
                                    "name, " +
                                    "task " +
                                    "FROM tasks.user_task ";

    public List<User> getAllUsers() {

        String sql = baseSelect;

        List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
        }
        return allUsers;
    }

    public String deleteEntry(User user) {

        String sqlDelete = "DELETE FROM tasks.user_task " +
                        "WHERE name = ?";

        getAllUsers();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlDelete);
            pstmt.setString(1, user.getName().trim().toUpperCase());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Something went wrong when connecting to database.");
            e.printStackTrace();
        }
        return "Entry removed";
    }

    public String save(User user) {
        String sql = "INSERT INTO tasks.user_task(name, task) " +
                "VALUES(?, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName().trim().toUpperCase());
            pstmt.setString(2, user.getTask().trim());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Something went wrong when connecting to database.");
            e.printStackTrace();
        }
        return user.getName() + " " + user.getTask();
    }

    public Optional<User> findUserNameByName(String name) {

        String sql = baseSelect + "WHERE name = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            System.err.println("Something went wrong when connection to database.");
            e.printStackTrace();
        }
        return Optional.empty();
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

    private List<User> mapResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("row_num"));
            user.setName(rs.getString("name"));
            user.setTask(rs.getString("task"));
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