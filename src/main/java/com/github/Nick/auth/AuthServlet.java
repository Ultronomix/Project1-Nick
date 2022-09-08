package com.github.Nick.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.users.UserResponse;

public class AuthServlet extends HttpServlet {

    private final AuthService authService;

    final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        //! System.out.println("U: " + credentials.getUsername() + " P: " + credentials.getPassword());
        try {
            Credentials credentials = jsonMapper.readValue(req.getInputStream(),Credentials.class);
            UserResponse responseBody = authService.authenticate(credentials);
            resp.setStatus(200); //* Ok general success

            HttpSession userSession = req.getSession();
            userSession.setAttribute("authUser", responseBody);

            resp.getWriter().write((jsonMapper.writeValueAsString(responseBody)));
            //TODO json exception response message
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);//* Bad request
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (AuthenticationException e) {
            resp.setStatus(401); //* Unauthorized
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 401);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException e) {
            resp.setStatus(500); //* Internal error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        }

        //! resp.getWriter().write(jsonMapper.writeValueAsString(loggedIn));
            
    }
    
}
