package com.github.Nick.users;

import java.util.ArrayList;
import java.util.List;

import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;
import com.github.Nick.common.exceptions.ResourcePersistenceException;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //? Get all users
    public List<UserResponse> getAllUsers() {

        //* Functional approach (more declarative)
        //? return userDAO.getAllUsers().stream()
        //?      .map(UserResponse::new).collect(Collectors.toList());

        //* Imperative (more explicit about what is being done)
        List<UserResponse> result = new ArrayList<>();
        List<User> users = userDAO.getAllUsers();

        for (User user : users) {
            result.add(new UserResponse(user));
        }

        return result;

    }

    //? Search for User by user_id
    public UserResponse getUserbyID (String id) {

        if (id == null || id.trim().length() <= 0) {
            throw new InvalidRequestException("An id must be provided");
        }
        // for uuid use try UUID uuid = uuid.fromString(id) return uuid
        //catch(IllegalArgumentException e)
        return userDAO.findUserById(id).map(UserResponse::new)
                        .orElseThrow(ResourceNotFoundException::new);
    }
    //? Search for User by username
    public UserResponse getUserbyUsername (String username) {

        if (username == null || username.trim().length() < 4) {
            throw new InvalidRequestException("A username must be at least 4 characters");
        }

        return userDAO.findUserByUsername(username).map(UserResponse::new)
                        .orElseThrow(ResourceNotFoundException::new);
    }

    //? Search for User by role
    public UserResponse getUserByRole (String role) {
        if (role == null || role.trim().length() <= 0) {
            throw new InvalidRequestException("A role must be provided.");
        }
        return userDAO.findUserByRole(role).map(UserResponse::new)
                        .orElseThrow(ResourceNotFoundException::new);
    }

    //? Register a new user
    public ResourceCreationResponse register (NewUserRequest newUser) {

        if (newUser == null || newUser.getUser_id() == null || newUser.getUsername() == null ||
            newUser.getEmail() == null || newUser.getPassword() == null || newUser.getGiven_name() == null ||
            newUser.getSurname() == null) {
            throw new InvalidRequestException("Provided request cannot be empty. Provide 'user_id', " +
                                            "'username', 'email', 'password', given_name' and 'surname'.");
        }
        
        if (newUser.getGiven_name().trim().length() <= 0 || newUser.getSurname().trim().length() <= 0) {
            throw new InvalidRequestException("A empty given name or surname");
        }

        if (newUser.getEmail().trim().length() <= 0) {
            throw new InvalidRequestException("Email cannot be empty");
        }

        if (newUser.getUsername().trim().length() < 4) {
            throw new InvalidRequestException("Username must be at least 4 characters");
        }

        if (newUser.getPassword().trim().length() < 6) {
            throw new InvalidRequestException("Password must be at least 6 characters");
        }

        if (userDAO.isEmailTaken(newUser.getEmail())) {
            throw new ResourcePersistenceException("Resource not persisted! The email is taken");
        }

        if (userDAO.isUsernameTaken(newUser.getUsername())) {
            throw new ResourcePersistenceException("Resource not persisted! The username is taken");
        }
        if (newUser.isIs_active() == false) {
            newUser.setIs_active(false);
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.save(userToPersist);

        return new ResourceCreationResponse(newUserId);
    }

    //? Delete a user
    public ResourceCreationResponse deleteUser (UpdateUserRequest updateUser) {

        if (updateUser == null ) {
            throw new InvalidRequestException("Provided request cannot be empty. Provide username to deactivate");
        }

        boolean userToDeactivate= updateUser.extractEntity().getIs_active();
        String userDeactivated = userDAO.deactivateUser(userToDeactivate, updateUser.extractEntity().getUsername());
        
        return new ResourceCreationResponse(userDeactivated + updateUser.getUsername());
    }

    //? Update user's active status
    public ResourceCreationResponse updateUser (UpdateUserRequest updateUser) {
        
        // check which info to update
        if (updateUser == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }
        if (updateUser.getEmail() != null && updateUser.getEmail().trim().length() > 3) {
            userDAO.updateUserEmail(updateUser.getEmail(), updateUser.getUsername());
        }
        if (updateUser.getGiven_name() != null && updateUser.getGiven_name().trim().length() > 0) {
            // userDAO.updateUserGivenName(updateUser.getGiven_Name(), updateUser.getUsername());
        }
        if (updateUser.getSurname() != null && updateUser.getSurname().trim().length() > 0) {
            // userDAO.updateUserSurname(updateUser.getSurname(), updateUser.getUsername());
        }
        if (updateUser.getIs_active() == true) {
            userDAO.updateUserActive(updateUser.getIs_active(), updateUser.getUsername());
        }
        if (updateUser.getRole() != null) {
            if (updateUser.getRole().toUpperCase().equals("FINANCE MANAGER")) {
                updateUser.setRole("3");
            } else if (updateUser.getRole().toUpperCase().equals("EMPLOYEE")) {
                updateUser.setRole("4");
            } else if (updateUser.getRole().toUpperCase().equals("ADMIN")) {
                updateUser.setRole("2");
            } else {
                throw new InvalidRequestException("Invalid role. Must be 'Admin', 'Finance Manager' " +
                                            "or 'Employee'.");
            }
            userDAO.updateUserRole(updateUser.getRole(), updateUser.getUsername());
        }

        return new ResourceCreationResponse("Updated User " + updateUser.getUsername());
        
    }
}