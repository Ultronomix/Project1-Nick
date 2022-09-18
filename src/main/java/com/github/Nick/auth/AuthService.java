package com.github.Nick.auth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;
import com.github.Nick.users.UserDAO;
import com.github.Nick.users.UserResponse;

public class AuthService {
    
    private static Logger logger = LogManager.getLogger(AuthService.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponse authenticate(Credentials credentials) {

        
        logger.info("Starting authentication at {}", LocalDateTime.now().format(format));

        if(credentials == null || credentials.getUsername().trim().length() < 4 || credentials.getPassword().trim().length() < 6) {
            logger.warn("Invalid credentials provided at {}", LocalDateTime.now().format(format));
            throw new InvalidRequestException("The provided credentials are invalid");
        }   
        
        try {
            boolean active = userDAO.isActive(credentials.getUsername(), credentials.getPassword());
            
            logger.info("Searching for user at {}", LocalDateTime.now().format(format));
            UserResponse user = userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                .map(UserResponse :: new).orElseThrow(AuthenticationException::new);
                
            logger.info("Checking if user is active at {}", LocalDateTime.now().format(format));
            if(active == true) {
                return user;
            } else {
                logger.warn("Inactive user tried to log in at {}", LocalDateTime.now().format(format));
                throw new InvalidRequestException("User unable to log in due to inactive");
            }

        } catch (ResourceNotFoundException e) {
            logger.warn("User was not found at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            throw new AuthenticationException();
        }
        
    }
}