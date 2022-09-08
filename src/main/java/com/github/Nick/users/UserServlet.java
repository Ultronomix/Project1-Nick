package com.github.Nick.users;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

public class UserServlet extends HttpServlet {

    private final UserService userService;

    final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // TODO inject a shared reference to a configured ObjectMapper

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession userSession = req.getSession(false);

        // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            resp.setStatus(401); // Unauthorized
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 401);
            errorResponse.put("message", "Requester is not authenticated with server, log in.");
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
            return;
        }

        String idToSearchFor = req.getParameter("id");
        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

        if (!requester.getRole().equals("CEO") && !requester.getUser_id().equals(idToSearchFor)) {
            resp.setStatus(403); // Forbidden
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 403);
            errorResponse.put("message", "Requester not permitted to communicate with this endpoint.");
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
            return;
        }

        String usernameToSearchFor = req.getParameter("username");
        String roleToSearch = req.getParameter("role");

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
            resp.setStatus(400);// bad request
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (ResourceNotFoundException e) {
            resp.setStatus(404); // Not Found
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 404);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException e) {
            resp.setStatus(500); //internal error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO registration logic
        // resp.getWriter().write(("Post to /users works"));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        try {
            ResourceCreationResponse responseBody = userService
                    .register(jsonMapper.readValue(req.getInputStream(), NewUserRequest.class));
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflit; indicate that provided resource could not be saved
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 409);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException e) {
            resp.setStatus(500); // * internal error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().format(format));
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        }

    }

}