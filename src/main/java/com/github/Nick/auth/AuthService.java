package com.github.Nick.auth;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.users.User;
import com.github.Nick.users.UserDAO;
import com.github.Nick.users.UserResponse;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //TODO edit conditions
    public UserResponse authenticate(Credentials credentials) {

        if(credentials == null || credentials.getUsername().trim().length() < 4 || credentials.getPassword().trim().length() < 6) {
            //TODO replace with a custom exception
            throw new InvalidRequestException("The provided credentials are invalid");
        }

        //TODO clean up in active check
        // Optional<User> user = userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword());

        UserResponse user = userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                .map(UserResponse :: new).orElseThrow(AuthenticationException::new);
        try{
            boolean active = userDAO.isActive(credentials.getUsername(), credentials.getPassword());
            
            if(active == true) {
                return user;
                // return userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                //     .map(UserResponse :: new).orElseThrow(AuthenticationException::new);
            } else {
                throw new RuntimeException("User is inactive");
            }

        } catch (NoSuchElementException e) {
            throw new AuthenticationException();
        }
        
    }
}