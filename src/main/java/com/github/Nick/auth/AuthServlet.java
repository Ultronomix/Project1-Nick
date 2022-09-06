package com.github.Nick.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.AuthException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.users.User;
import com.github.Nick.users.UserDAO;
import com.github.Nick.users.UserResponse;

public class AuthServlet extends HttpServlet {

    private final AuthService authService;

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // resp.getWriter().write("/auth work");
        // TODO create password
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        // System.out.println("U: " + credentials.getUsername() + " P: " + credentials.getPassword());
        try {
            Credentials credentials = jsonMapper.readValue(req.getInputStream(),Credentials.class);
            UserResponse responseBody = authService.authenticate(credentials);
            resp.setStatus(200); //ok
            resp.getWriter().write((jsonMapper.writeValueAsString(responseBody)));
            //TODO json exception response message
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// bad request
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (AuthenticationException e) {
            resp.setStatus(401); // unauthorized
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 401);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException e) {
            resp.setStatus(500); //internal error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        }

        // resp.getWriter().write(jsonMapper.writeValueAsString(loggedIn));
            
    }
    
}
