package com.github.Nick.users;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.ErrorResponse;
import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

public class UserServlet extends HttpServlet {

    private final UserService userService;

    // TODO inject a shared reference to a configured ObjectMapper

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        // Access the HTTP session on the request
        HttpSession userSession = req.getSession(false);

        // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }

        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

        String idToSearchFor = req.getParameter("id");
        String roleToSearchFor = req.getParameter("role");
        String usernameToSearchFor = req.getParameter("username");
        String roleToSearch = req.getParameter("role");

        if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("ADMIN")) && !requester.getUser_id().equals(idToSearchFor) && !requester.getRole().equals(roleToSearchFor)) {
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
            resp.setStatus(400);// bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {
            resp.setStatus(404); // Not Found
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); //internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
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
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflit; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    //TODO
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //! delete
        resp.getWriter().write("In Progress\n");

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

         // Access the HTTP session on the request
        HttpSession userSession = req.getSession(false);

         // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }

        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

        // Only CEO and ADMIN access
        if (!requester.getRole().equals("CEO") && !requester.getRole().equals("ADMIN")) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        // Find user to update
        String idToSearchFor = req.getParameter("id");
        UserResponse foundUser = userService.getUserbyID(idToSearchFor);
        resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));

        // Get updated info
        try {
            ResourceCreationResponse responseBody = userService
                    .updateUser(jsonMapper.readValue(req.getInputStream(), UpdateUserRequest.class), idToSearchFor);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflit; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
        resp.getWriter().write("\nEmail is: "+ requester.getEmail());
    }
}