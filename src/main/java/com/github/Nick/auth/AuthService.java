package com.github.Nick.auth;

import java.util.List;

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
        if(credentials == null || credentials.getUsername().trim().length() < 4 || credentials.getPassword().trim().length() < 8) {
            throw new InvalidRequestException("The provided credentials are invalid"); //TODO replace with a custom exception
        }
        return userDAO.findUserNameByNameErs(credentials.getUsername(), credentials.getPassword())
                .map(UserResponse :: new)
                .orElseThrow(AuthenticationException::new);
    }
}