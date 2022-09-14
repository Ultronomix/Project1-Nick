package com.github.Nick.reimbursements;

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
import com.github.Nick.users.UserResponse;

public class ReimbServlet extends HttpServlet {

    private final ReimbService reimbService;

    public ReimbServlet(ReimbService reimbService) {
        this.reimbService = reimbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO add log
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            // TODO add log
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        String idToSearchFor = req.getParameter("id");
        String statusToSearchFor = req.getParameter("status");
        String typeToSearchFor = req.getParameter("type");

        if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("FINANCE MANAGER")) && !requester.getUser_id().equals(idToSearchFor)) { // && !requester.getRole().equals(idToSearchFor)) {
            // TODO add log
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        
        try {
            if (idToSearchFor == null && statusToSearchFor == null && typeToSearchFor == null) {
                // TODO add log
                List<ReimbResponse> allReimb = reimbService.getAllReimb();
                resp.getWriter().write(jsonMapper.writeValueAsString(allReimb));
                //! resp.getWriter().write("\nGet all reimburse request");
            }
            if (idToSearchFor != null) {
                // TODO add log
                ReimbResponse foundRequest = reimbService.getReimbById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundRequest));
                //! resp.getWriter().write("\nGet reimburse request by id");
            }
            if (statusToSearchFor != null) {
                // TODO add log
                List<ReimbResponse> foundStatus = reimbService.getReimbByStatus(statusToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundStatus));
                //! resp.getWriter().write("\nGet reimburse by status");
            }
            if (typeToSearchFor != null) {
                // TODO add log
                List<ReimbResponse> foundType = reimbService.getReimbByType(typeToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundType));
                //! resp.getWriter().write("\nGet reimburse by type");
            }
        } catch (InvalidRequestException | JsonMappingException e) {
            // TODO add log
            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {
            // TODO add log
            resp.setStatus(404);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {
            // TODO add log
            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }

        resp.getWriter().write("Reimb GET authorization end");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper
                    .writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        resp.getWriter().write("Post to /reimb work");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO add log
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            // TODO add log
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper
                    .writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        String idToSearchFor= req.getParameter("id");

        if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("FINANCE MANGER")) 
          && !requester.getUser_id().equals(idToSearchFor)) {
            // TODO log
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
          }

          ReimbResponse foundReimb = reimbService.getReimbById(idToSearchFor);
          resp.getWriter().write(jsonMapper.writeValueAsString(foundReimb));

        try {
            ResourceCreationResponse responseBody = 
                reimbService.updateReimb(jsonMapper.readValue(req.getInputStream(), UpdateReimbRequest.class), idToSearchFor);
        } catch (InvalidRequestException | JsonMappingException e) {
            // TODO add log
            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            // TODO add log
            resp.setStatus(409);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            // TODO add log
            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
        
        resp.getWriter().write("\nPut to /reimb end");
    }
}
