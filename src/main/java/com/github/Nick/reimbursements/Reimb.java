package com.github.Nick.reimbursements;

import java.util.Objects;

public class Reimb {
    
    private String reimb_id;
    private double amount;
    private String submitted;
    private String resolved;
    private String description;
    private String payment_id;
    private String author_id; //? links to user_id
    private String resolver_id; //? links to user_id
    private String status; //? links to reimbursement statuses
    private String type; //? links to reimbursement type

    public String getReimb_id() {
        return this.reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSubmitted() {
        return this.submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getResolved() {
        return this.resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_id() {
        return this.payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getAuthor_id() {
        return this.author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getResolver_id() {
        return this.resolver_id;
    }

    public void setResolver_id(String resolver_id) {
        this.resolver_id = resolver_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
        
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null ||getClass() != o.getClass()) {return false;}
        Reimb reimb = (Reimb) o;
        return Objects.equals(reimb_id, reimb.reimb_id) && Objects.equals(amount, reimb.amount)
            && Objects.equals(submitted, reimb.submitted) && Objects.equals(resolved, reimb.resolved)
            && Objects.equals(description, reimb.description) && Objects.equals(payment_id, reimb.payment_id)
            && Objects.equals(author_id, reimb.author_id) && Objects.equals(resolver_id, reimb.resolver_id)
            && Objects.equals(status, reimb.status) && Objects.equals(type, reimb.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimb_id, amount, submitted, resolved, description, payment_id, author_id,
                            resolver_id, status, type);                   
    }

    @Override
    public String toString() {
        return "Reimbursement {" +
                "reimb_id = '" + reimb_id + "' " +
                "amount = '" + amount + "' " +
                "submitted = '" + submitted + "' " +
                "resolved = '" + resolved + "' " +
                "description = '" + description + "' " +
                "payment_id = '" + payment_id + "' " +
                "author_id = '" + author_id + "' " +
                "resolver_id = '" + resolver_id + "' " +
                "status = '" + status + "' " +
                "type = '" + type + "'}";
    }
}
