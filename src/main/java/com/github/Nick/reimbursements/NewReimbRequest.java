package com.github.Nick.reimbursements;

import com.github.Nick.common.Request;

public class NewReimbRequest implements Request<Reimb> {

    private String reimb_id;
    private int amount;
    private String description;
    private String payment_id;
    private String type; //? links to reimbursement type

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NewReimbRequest {" + 
                "reimb_id = '" + reimb_id + "' " + 
                "amount = '" + amount + "' " +
                "description = '" + description + "' " +
                "payment_id = '" + payment_id + "' " +
                "type = '" + type + "'}";
    }

    @Override
    public Reimb extractEntity() {
        Reimb extractEntity = new Reimb();
        extractEntity.setReimb_id(this.reimb_id);
        extractEntity.setAmount(this.amount);
        extractEntity.setDescription(this.description);
        extractEntity.setPayment_id(this.payment_id);
        extractEntity.setType(this.type);
        return extractEntity;
    }
}
