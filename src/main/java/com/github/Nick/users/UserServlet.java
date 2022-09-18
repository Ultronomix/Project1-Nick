package com.github.Nick.users;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

public class UserServlet extends HttpServlet {
    
    private static Logger logger = LogManager.getLogger(UserDAO.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final UserService userService;

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("Do Get received at {}", LocalDateTime.now().format(format));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        // Access the HTTP session on the request
        HttpSession userSession = req.getSession(false);

        // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            logger.warn("No session found at {}", LocalDateTime.now().format(format));
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }

        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

        String idToSearchFor = req.getParameter("id");
        String roleToSearchFor = req.getParameter("role");
        String usernameToSearchFor = req.getParameter("username");
        String roleToSearch = req.getParameter("role");

        if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("ADMIN")) 
          && !requester.getUser_id().equals(idToSearchFor) && !requester.getRole().equals(roleToSearchFor)) {
            logger.warn("Unauthorized user request at {}", LocalDateTime.now().format(format));
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            if(idToSearchFor == null && usernameToSearchFor == null && roleToSearch == null) {
                List<UserResponse> allUsers = userService.getAllUsers();
                resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
            }
            if(idToSearchFor != null) {
                UserResponse foundUser = userService.getUserbyID(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }
            if(usernameToSearchFor != null) {
                UserResponse foundUser = userService.getUserbyUsername(usernameToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }
            if(roleToSearch != null) {
                UserResponse foundUser = userService.getUserByRole(roleToSearch);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }
        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Invalid request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);// bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {
            logger.warn("Resource not found at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(404); // Not Found
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500); //internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        logger.info("DO Post request received", LocalDateTime.now().format(format));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

         // Access the HTTP session on the request
         HttpSession userSession = req.getSession(false);

         // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            logger.warn("Session not created at {}", LocalDateTime.now().format(format));
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }

        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");
        
        // Only CEO and ADMIN access
        if (!requester.getRole().equals("CEO") && !requester.getRole().equals("ADMIN")) {
            logger.warn("Unauthorized user request received at {}", LocalDateTime.now().format(format));
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            if (req.getInputStream().equals(null)) {throw new InvalidRequestException("Empty request. Enter user's information.");}

            ResourceCreationResponse responseBody = userService
                    .register(jsonMapper.readValue(req.getInputStream(), NewUserRequest.class));

            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Invalid request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            logger.warn("Authentication error at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        logger.info("Do Put request received at {}", LocalDateTime.now().format(format));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

         // Access the HTTP session on the request
        HttpSession userSession = req.getSession(false);

         // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            logger.warn("Session not created at {}", LocalDateTime.now().format(format));
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }

        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");
        
        // Only CEO and ADMIN access
        if (!requester.getRole().equals("CEO") && !requester.getRole().equals("ADMIN")) {
            
            logger.warn("Unauthorized user request at {}", LocalDateTime.now().format(format));
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
                   
        // Get updated info
        try {
            UpdateUserRequest input = jsonMapper.readValue(req.getInputStream(), UpdateUserRequest.class);
            String username = input.extractEntity().getUsername();

            ResourceCreationResponse responseBody = userService
                    .updateUser(input);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

            resp.getWriter().write(jsonMapper.writeValueAsString(userService.getUserbyUsername(username)));

        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Invalid request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            logger.warn("Authentication error at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error connecting with database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        logger.info("Do Delete request received at {}", LocalDateTime.now().format(format));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

         // Access the HTTP session on the request
        HttpSession userSession = req.getSession(false);
        
        // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            logger.warn("Session not created at {}", LocalDateTime.now().format(format));
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }
        
        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");
        
        if (!requester.getRole().equals("CEO") && !requester.getRole().equals("ADMIN")) {
            logger.warn("Unauthorized user request received at {}", LocalDateTime.now().format(format));
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        
        try {
            if (req.getInputStream() == null) {throw new InvalidRequestException("Empty request. Enter user's information.");}
            
            UpdateUserRequest input = jsonMapper.readValue(req.getInputStream(), UpdateUserRequest.class);
            ResourceCreationResponse responseBody = userService.deleteUser(input);

            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Invalid request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            logger.warn("Authentication error at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }
}