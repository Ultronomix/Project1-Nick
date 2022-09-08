package com.github.Nick.users;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;
import com.github.Nick.common.exceptions.ResourcePersistenceException;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

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

    public UserResponse getUserbyID (String id) {
        if (id == null || id.trim().length() <= 0) {
            throw new InvalidRequestException("An id must be provided");
        }
        // for uuid use try UUID uuid = uuid.fromString(id) return uuid
        //catch(IllegalArgumentException e)
        return userDAO.findUserById(id).map(UserResponse::new)
                        .orElseThrow(ResourceNotFoundException::new);
    }

    public ResourceCreationResponse register (NewUserRequest newUser) {
        //TODO edit with database

        if (newUser == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }

        if (newUser.getGiven_name().trim().length() <= 0 || newUser.getGiven_name() == null || 
            newUser.getSurname().trim().length() <= 0 || newUser.getSurname() == null) {
            throw new InvalidRequestException("A empty given name or surname");
        }

        if (newUser.getEmail() == null || newUser.getEmail().trim().length() <= 0) {
            throw new InvalidRequestException("Email cannot be empty");
        }

        if (newUser.getUsername() == null || newUser.getUsername().trim().length() < 4) {
            throw new InvalidRequestException("Username must be at least 4 characters");
        }

        if (newUser.getPassword() == null || newUser.getPassword().trim().length() < 6) {
            throw new InvalidRequestException("Password must be at least 6 characters");
        }

        if (userDAO.isEmailTaken(newUser.getEmail())) {
            throw new ResourcePersistenceException("Resource not persisted! The email is taken");
        }

        if (userDAO.isUsernameTaken(newUser.getUsername())) {
            throw new ResourcePersistenceException("Resource not persisted! The username is taken");
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.save(userToPersist);
        return new ResourceCreationResponse(newUserId);
    }
}