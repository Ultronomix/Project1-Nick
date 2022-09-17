package com.github.Nick.reimbursements;

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
import com.github.Nick.users.UserResponse;

public class ReimbServlet extends HttpServlet {
    
    private static Logger logger = LogManager.getLogger(ReimbService.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final ReimbService reimbService;

    public ReimbServlet(ReimbService reimbService) {
        this.reimbService = reimbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        logger.info("DO GET request recived at {}", LocalDateTime.now().format(format));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            logger.warn("Error with session at {}", LocalDateTime.now().format(format));
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        String idToSearchFor = req.getParameter("id");
        String statusToSearchFor = req.getParameter("status");
        String typeToSearchFor = req.getParameter("type");

        if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("FINANCE MANAGER")) && !requester.getUser_id().equals(idToSearchFor)) { 
            logger.warn("User not allowed at {}", LocalDateTime.now().format(format));
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        
        try {
            if (idToSearchFor == null && statusToSearchFor == null && typeToSearchFor == null) {
                
                List<ReimbResponse> allReimb = reimbService.getAllReimb();
                resp.getWriter().write(jsonMapper.writeValueAsString(allReimb));
            }
            if (idToSearchFor != null) {
                
                List<ReimbResponse> foundRequest = reimbService.getReimbByUserId(requester.getUser_id());
                resp.getWriter().write(jsonMapper.writeValueAsString(foundRequest));
            }
            if (statusToSearchFor != null) {
            
                List<ReimbResponse> foundStatus = reimbService.getReimbByStatus(statusToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundStatus));
            }
            if (typeToSearchFor != null) {
        
                List<ReimbResponse> foundType = reimbService.getReimbByType(typeToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundType));
            }
        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Error with request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {
            logger.warn("Not resource found at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(404);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error with receiving from database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }

        logger.info("End of DO GET at {}", LocalDateTime.now().format(format));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("Do POST request received at {}", LocalDateTime.now().format(format));
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            logger.warn("Error with session {}", LocalDateTime.now().format(format));
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper
                    .writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        try {

            ResourceCreationResponse responseBody = 
                reimbService.createRequest(jsonMapper.readValue(req.getInputStream(), NewReimbRequest.class), 
                                            requester.getUser_id());
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Error with request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            logger.warn("Error with authenticating at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(409);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error connection to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }

        logger.info("End of DO POST");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("DO PUT request received");
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if (reimbSession == null) {
            logger.warn("Error with session at {}", LocalDateTime.now().format(format));
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper
                    .writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");
    
        try {
            if (!requester.getRole().equals("CEO") && !requester.getRole().equals("FINANCE MANAGER")) {

                ResourceCreationResponse responseBody = 
                    reimbService.updateUserReimb(jsonMapper.readValue(req.getInputStream(), UpdateReimbRequest.class));
                resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

                return;
            }

            ResourceCreationResponse responseBody = 
                reimbService.updateReimb(jsonMapper.readValue(req.getInputStream(), UpdateReimbRequest.class), requester.getUser_id());
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {
            logger.warn("Error with request at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            logger.warn("Error with authenticating at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(409);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            logger.warn("Error connecting to database at {}, error message {}", LocalDateTime.now().format(format), e.getMessage());
            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }

        logger.info("DO PUT end");

    }
}
