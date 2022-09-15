package com.github.Nick.reimbursements;

import java.util.ArrayList;
import java.util.List;

import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;
import com.github.Nick.common.exceptions.ResourcePersistenceException;

public class ReimbService {

    private final ReimbDAO reimbDAO;

    public ReimbService (ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }
    
    public List<ReimbResponse> getAllReimb () {

        // TODO add log
        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = reimbDAO.getAllReimb();

        for (Reimb reimb : reimbs) {
            result.add(new ReimbResponse(reimb));
        }

        return result;
        // TODO add log
    }

    public ReimbResponse getReimbById (String id) {

        // TODO add log
        if (id == null || id.trim().length() <= 0) {
            // TODO add log
            throw new InvalidRequestException("A user's id must be provided");
        }

        return reimbDAO.getReimbById(id).map(ReimbResponse::new).orElseThrow(ResourceNotFoundException::new);
        // TODO add logs
    
    }

    public List<ReimbResponse> getReimbByStatus (String status) {

        // TODO add log
        if (status == null || (!status.toUpperCase().trim().equals("APPROVED") 
                            && !status.toUpperCase().trim().equals("PENDING") 
                            && !status.toUpperCase().trim().equals("DENIED"))) {
            // TODO add log
            throw new InvalidRequestException("Status cannot be empty. Enter 'Approved', 'Pending', " +
                                                " or 'Denied'");
        }
        // TODO add log

        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = reimbDAO.getReimbByStatus(status);

        for (Reimb reimb : reimbs) {
            result.add(new ReimbResponse(reimb));
        }
        
        return result;
        // TODO add log
    }

    public List<ReimbResponse> getReimbByType (String type) {

        // TODO add log
        if (type == null || (!type.toUpperCase().trim().equals("LODGING") 
                          && !type.toUpperCase().trim().equals("TRAVEL")
                          && !type.toUpperCase().trim().equals("FOOD")
                          && !type.toUpperCase().trim().equals("OTHER"))) {
            // TODO add log
            throw new InvalidRequestException("Type must be 'Lodging', 'Travel', " +
                                              "'Food', or 'Other'");
            
        }

        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = reimbDAO.getReimbByType(type);

        for (Reimb reimb : reimbs) {
            result.add(new ReimbResponse(reimb));
        }

        return result;
        // TODO add log
    }

    public ResourceCreationResponse updateReimb (UpdateReimbRequest updateReimb, String reimbIdToSearchFor, String resolver_id) {

        // TODO add log
        if (updateReimb == null) {
            // TODO add log
            throw new InvalidRequestException("Provide request payload");
        }

        String reimbToUpdate = updateReimb.extractEntity().getStatus().toUpperCase();

        if(reimbToUpdate.equals("APPROVED")) {
            reimbToUpdate = "100001";
        } else if (reimbToUpdate.equals("DENIED")) {
            reimbToUpdate = "100003";
        }

        String update = reimbDAO.updateRequestStatus(reimbToUpdate, reimbIdToSearchFor, resolver_id);

        return new ResourceCreationResponse(update);
    }

    public ResourceCreationResponse updateUserReimb (UpdateReimbRequest updateReimb, String reimbId) {
        
        // TODO add logs
        if (updateReimb == null) {
            // TODO add log
            throw new InvalidRequestException("Provide request payload");
        }

        if (!reimbDAO.isPending(reimbId)) {
            // TODO add log
            throw new ResourcePersistenceException("Request is not pending.");
        }

        double newAmount = updateReimb.extractEntity().getAmount();
        String newDescription = updateReimb.extractEntity().getDescription();
        String newType = updateReimb.extractEntity().getType();

        System.out.println(newAmount);

        if (newAmount > 0) {
            if (newAmount > 9999.99) {
                // TODO add log
                throw new InvalidRequestException("Amount must be below 10,000.");
            }

            reimbDAO.updateUserAmount(reimbId, newAmount);

        }
        if (newDescription != null) {

            reimbDAO.updateUserDescription(reimbId, newDescription);

        }
        if (newType != null) {
            if (!newType.toUpperCase().equals("LODGING") && !newType.toUpperCase().equals("TRAVEL")
                && !newType.toUpperCase().equals("FOOD") && !newType.toUpperCase().equals("Other")) {
                // TODO add log
                throw new InvalidRequestException("Type must be 'Lodging', 'Travel', 'Food' " +
                                                        "or 'Other'");
            }
            if (newType.toUpperCase().equals("LODGING")) {
                    newType = "200001";
            }
            if (newType.toUpperCase().equals("TRAVEL")) {
                    newType = "200002";
            }
            if (newType.toUpperCase().equals("FOOD")) {
                    newType = "200003";
            }
            if (newType.toUpperCase().equals("OTHER")) {
                    newType = "200004";
            }

            reimbDAO.updateUserType(reimbId, newType);
        }
        // TODO add log
        return new ResourceCreationResponse("Updated requests") ;
    }

    public ResourceCreationResponse createRequest (NewReimbRequest newRequest, String user_id) {

        // TODO add log
        if (newRequest == null) {
            // TODO add log
            throw new InvalidRequestException("Provide request payload was empty.");
        }
        if (newRequest.getReimb_id() == null || newRequest.getReimb_id().trim().length() <= 0) {
            // TODO add log
            throw new InvalidRequestException("Reimbursement id cannot be empty.");

        }
        if (newRequest.getAmount() <= 0.0 || newRequest.getAmount() > 9999.99) {
            // TODO add log
            throw new InvalidRequestException("Amount must be between 0 and 10,000.00.");

        }
        if (newRequest.getDescription() == null || newRequest.getDescription().trim().length() <= 0) {
            // TODO add log
            throw new InvalidRequestException("Description cannot be empty.");

        }
        if (newRequest.getPayment_id() == null || newRequest.getReimb_id().trim().length() <= 0) {
            // TODO add log
            throw new InvalidRequestException("Payment id cannot be empty.");

        }
        if (newRequest.getType() == null || newRequest.getReimb_id().trim().length() <= 0) {
            // TODO add log
            throw new InvalidRequestException("Type cannot be empty. Enter 'Lodging', 'Travel', " +
                                                "'Food', or 'Other'.");

        }
        if (newRequest.getType().trim().toUpperCase().equals("LODGING")) {

            newRequest.setType("200001");

        } else if (newRequest.getType().trim().toUpperCase().equals("TRAVEL")) {

            newRequest.setType("200002");

        } else if (newRequest.getType().trim().toUpperCase().equals("FOOD")) {

            newRequest.setType("200003");

        } else if (newRequest.getType().trim().toUpperCase().equals("OTHEr")) {

            newRequest.setType("200004");

        } else {
            // TODO add log
            throw new InvalidRequestException("Type must be either 'Lodging', 'Travel', " +
                                                "'Food', or 'Other'");
        }

        // TODO add log
        Reimb requestToMake = newRequest.extractEntity();
        String requestCreated = reimbDAO.newRequest(requestToMake, user_id);
        // TODO add log
        return new ResourceCreationResponse(requestCreated);
    }
}
