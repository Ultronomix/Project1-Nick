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

    public ResourceCreationResponse updateReimb (UpdateReimbRequest updateReimb, String idToSearchFor) {

        // TODO add log
        if (updateReimb == null) {
            // TODO add log
            throw new InvalidRequestException("Provide request payload");
        }

        String reimbToUpdate = updateReimb.extractEntity().getStatus();
        // TODO create update method
        return null;
    }

}
