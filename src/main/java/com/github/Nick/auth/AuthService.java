package com.github.Nick.auth;

import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.InvalidRequestException;
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
        return userDAO.findUserByUsername(credentials.getUsername())
                .map(UserResponse :: new).orElseThrow(AuthenticationException::new);
    }
}