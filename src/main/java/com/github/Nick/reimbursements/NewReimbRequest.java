package com.github.Nick.reimbursements;

import com.github.Nick.common.Request;

public class NewReimbRequest implements Request<Reimb> {

    private String reimb_id;
    private int amount;
    private String submitted;
    // private String resolved; //? set with resolver_id
    private String description;
    private String payment_id;
    private String author_id; // ? links to user_id //? Get id from cookie
    // private String resolver_id; // ? links to user_id 
    private String type_id; // ? links to reimbursement type

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

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
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

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    

    @Override
    public String toString() {
        return "NewReimbRequest {" + 
                "reimb_id = '" + reimb_id + "' " + 
                "amount = '" + amount + "' " +
                "submitted = " + submitted + "' " + 
                "description = '" + description + "' " +
                "payment_id = '" + payment_id + "' " +
                "author_id = '" + author_id + "' " +
                "type_id = '" + type_id + "'}";
    }

    @Override
    public Reimb extractEntity() {
        Reimb extractEntity = new Reimb();
        extractEntity.setReimb_id(this.reimb_id);
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setDescription(this.description);
        extractEntity.setPayment_id(this.payment_id);
        extractEntity.setAuthor_id(this.author_id);
        extractEntity.setType_id(this.type_id);
        return null;
    }
}
