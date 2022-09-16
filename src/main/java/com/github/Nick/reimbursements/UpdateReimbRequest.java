package com.github.Nick.reimbursements;

import com.github.Nick.common.Request;

public class UpdateReimbRequest implements Request<Reimb> {

    //? Search for reimbursement
    private String reimbId;

    //? Finance Manager
    private String status;
    
    //? User
    private int amount;
    private String description;
    private String type;

    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }
    
    public String getReimbId() {
        return reimbId;
    }

    public void setReimbId(String reimbId) {
        this.reimbId = reimbId;
    }
    
    @Override
    public String toString() {
        return "UpdateReimbRequet [" +
        "Reimb = '" + reimbId + "' updated]";
    }
    
    @Override
    public Reimb extractEntity() {
        Reimb extractedEntity = new Reimb();
        extractedEntity.setReimb_id(this.reimbId);
        extractedEntity.setStatus(this.status);
        extractedEntity.setAmount(this.amount);
        extractedEntity.setDescription(this.description);
        extractedEntity.setType(this.type);
        return extractedEntity;
    }
}
