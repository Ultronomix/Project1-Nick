package com.github.Nick.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.ErrorResponse;
import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.DataSourceException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.users.UserResponse;

public class AuthServlet extends HttpServlet {

    private final AuthService authService;

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

            // Establish on HTTP session that is implicitly attached to the response as a cookie
            // The web client will automatically attach this cookie to requests to the server
            HttpSession userSession = req.getSession();
            userSession.setAttribute("authUser", responseBody);

            resp.getWriter().write((jsonMapper.writeValueAsString(responseBody)));
            //TODO json exception response message
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);//* Bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(401); //* Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); //* Internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate(); // logs out the user
    }
}
