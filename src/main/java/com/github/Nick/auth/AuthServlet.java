package com.github.Nick.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.ErrorResponse;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.users.UserResponse;

public class AuthServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(AuthServlet.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final AuthService authService;

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A POST request was received by /Reimbursement/auth at {}", LocalDateTime.now().format(format));

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        
        try {
            Credentials credentials = jsonMapper.readValue(req.getInputStream(),Credentials.class);
            UserResponse responseBody = authService.authenticate(credentials);
            resp.setStatus(200); //* Ok general success

            // Establish on HTTP session that is implicitly attached to the response as a cookie
            // The web client will automatically attach this cookie to requests to the server
            logger.info("Establishing session for user: {}", responseBody.getUsername());
            HttpSession userSession = req.getSession();
            userSession.setAttribute("authUser", responseBody);
            
            resp.getWriter().write((jsonMapper.writeValueAsString(responseBody)));
            
            logger.info("Post request successfully processed at {}", LocalDateTime.now().format(format));
            
            //* json exception response message
        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Invalid processing request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);//* Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            logger.warn("Failed login at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(401); //* Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("A data source error occurs at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500); //* Internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        } 
        
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate(); // logs out the user
        resp.getWriter().write("User has been logged out.");
    }
}
