package com.github.Nick.reimbursements;

import com.github.Nick.common.Request;

public class UpdateReimbRequest implements Request<Reimb> {

    private String status;
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    
    
    @Override
    public String toString() {
        return "UpdateReimbRequet [" +
               "status = '" + status + "'']";
    }

    @Override
    public Reimb extractEntity() {
        Reimb extractedEntity = new Reimb();
        extractedEntity.setStatus(this.status);
        return extractedEntity;
    }

    
}
