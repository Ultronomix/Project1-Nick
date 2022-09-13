package com.github.Nick.reimbursements;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Nick.common.ErrorResponse;

public class ReimbServlet extends HttpServlet {

    private final ReimbService reimbService;

    public ReimbServlet(ReimbService reimbService) {
        this.reimbService = reimbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        resp.getWriter().write("Get to /reimb work");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        resp.getWriter().write("Post to /reimb work");
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        resp.getWriter().write("Put to /reimb work");
    }
}
