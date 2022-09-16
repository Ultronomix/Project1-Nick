package com.github.Nick.reimbursements;

import java.util.ArrayList;
import java.util.List;

import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourceNotFoundException;

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

    public List<ReimbResponse> getReimbByUserId (String id) {

        // TODO add log
        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = reimbDAO.getReimbByUserID(id);
        
        if (reimbs.isEmpty()) {
            // TODO add log
            throw new ResourceNotFoundException();
        }

        for (Reimb reimb : reimbs) {
            result.add(new ReimbResponse(reimb));
        }
        
        return result;
        // TODO add log
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

        List<ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = reimbDAO.getReimbByStatus(status);

        if (reimbs.isEmpty()) {
            // TODO add log
            throw new ResourceNotFoundException();
        }

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

    public ResourceCreationResponse updateReimb (UpdateReimbRequest updateReimb, String resolver_id) {

        // TODO add log
        if (updateReimb == null) {
            // TODO add log
            throw new InvalidRequestException("Provide request payload");
        }

        String reimbToUpdate = updateReimb.extractEntity().getReimb_id();
        String reimbStatueChange = updateReimb.extractEntity().getStatus().toUpperCase();

        if(reimbStatueChange.equals("APPROVED")) {
            reimbStatueChange = "100001";
        } else if (reimbStatueChange.equals("DENIED")) {
            reimbStatueChange = "100003";
        }

        String updated = reimbDAO.updateRequestStatus(reimbStatueChange, reimbToUpdate, resolver_id);

        return new ResourceCreationResponse(updated);
        // TODO add log
    }

    public ResourceCreationResponse updateUserReimb (UpdateReimbRequest updateReimb) {
        
        // TODO add logs
        if (updateReimb == null || updateReimb.extractEntity().getStatus() != null) {
            // TODO add log
            throw new InvalidRequestException("Request can not be empty and user cannot change the status.");
        }
        
        String reimbIdToSearch = updateReimb.extractEntity().getReimb_id();
        double newAmount = updateReimb.extractEntity().getAmount();
        String newDescription = updateReimb.extractEntity().getDescription();
        String newType = updateReimb.extractEntity().getType();

        if (!reimbDAO.isPending(reimbIdToSearch)) {
            // TODO add log
            throw new InvalidRequestException("Request is not pending.");
        }

        if (newAmount > 0) {
            if (newAmount > 9999.99 || newAmount == 0.0) {
                // TODO add log
                throw new InvalidRequestException("Amount must be between 0 and 10,000");
            }
            reimbDAO.updateUserAmount(reimbIdToSearch, newAmount);
        }
        if (newDescription != null) {
            reimbDAO.updateUserDescription(reimbIdToSearch, newDescription);
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
            reimbDAO.updateUserType(reimbIdToSearch, newType);
        }
        return new ResourceCreationResponse("Updated requests") ;
        // TODO add log
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
        return new ResourceCreationResponse(requestCreated);
        // TODO add log
    }
}
