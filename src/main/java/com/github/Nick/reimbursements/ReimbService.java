package com.github.Nick.reimbursements;

import java.util.List;

public class ReimbService {

    private final ReimbDAO reimbDAO;

    public ReimbService (ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }
    
    public List<ReimbResponse> getAllReimb () {
        return null;
    }
}
