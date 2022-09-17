package com.github.Nick.users;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.Nick.common.datasource.ConnectionFactory;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

//* DAO = Data Access Object 
public class UserDAO {
    
    private static Logger logger = LogManager.getLogger(UserDAO.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String baseSelect = "SELECT eu.user_id, eu.username, eu.email, eu.password, " +
                                        "eu.given_name, eu.surname, " +
                                        "eu.is_active, eur.role " +
                                        "FROM ers_users eu " +
                                        "JOIN ers_user_roles eur ON eu.role_id = eur.role_id ";

    public List<User> getAllUsers() {

        String sql = baseSelect;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return mapResultSet(rs);

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }

    }

    //TODO convert to new database
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
    
    public String save(User user) {
        String sql = "INSERT INTO ers_users (user_id, username, email, password, given_name, surname, is_active, role_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, '3')";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("id: " + user.getUser_id());
            pstmt.setString(1, user.getUser_id().trim());
            System.out.println("username: " + user.getUsername());
            pstmt.setString(2, user.getUsername().trim());
            System.out.println("email: " + user.getEmail());
            pstmt.setString(3, user.getEmail().trim());
            System.out.println("password: " + user.getPassword());
            pstmt.setString(4, user.getPassword().trim());
            System.out.println("name: " + user.getGiven_name());
            pstmt.setString(5, user.getGiven_name().trim());
            System.out.println("surname: " + user.getSurname());
            pstmt.setString(6, user.getSurname().trim());
            System.out.println("Active: " + user.getIs_active());
            pstmt.setBoolean(7, user.getIs_active());
            
            pstmt.executeUpdate();

            return user.getUsername() + " added.";

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        String sql = baseSelect + "WHERE eu.username = ? and eu.password = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }
    // for uuid change string to uuid
    public Optional<User> findUserById(String id) {

        String sql = baseSelect + "WHERE eu.user_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            //for uuid change setString to setObject
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public Optional<User> findUserByUsername(String username) {

        String sql = baseSelect + "WHERE eu.username = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public Optional<User> findUserByRole(String role) {

        String sql = baseSelect + "WHERE eur.role = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);

            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    // Check if user is active
    public boolean isActive (String username, String password) {
        try{
            Optional<User> user = findUserByUsernameAndPassword(username, password);

            if(user.get().getIs_active()) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchElementException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new ResourceNotFoundException();
        }
    }

    public boolean isUsernameTaken (String username) {
        return findUserByUsername(username).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {

        String sql = baseSelect + "WHERE eu.email = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    public boolean isEmailTaken (String email) {
        return findUserByEmail(email).isPresent();
    }

    public String updateUser (UpdateUserRequest updateUserRequest) {
        return null;
    }

    public String updateUserEmail (String email, String user_id) {

        String sql = "UPDATE ers_users SET email = ? WHERE user_id = ";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, user_id);

            pstmt.executeUpdate();

            return "Email Updated";

        } catch (SQLException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new DataSourceException(e);
        }
    }

    private List<User> mapResultSet(ResultSet rs) throws SQLException {

        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User user = new User();
            user.setUser_id(rs.getString("user_id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            //! user.setPassword(rs.getString("password"));
            user.setGiven_name(rs.getString("given_name"));
            user.setSurname(rs.getString("surname"));
            user.setIs_active(rs.getBoolean("is_active"));
            user.setRole(rs.getString("role"));

            users.add(user);
        }
        return users;
    }
}

/** DAO that gets all users from the database, delete a user
 * from the database, or saves a user to the database.
 */