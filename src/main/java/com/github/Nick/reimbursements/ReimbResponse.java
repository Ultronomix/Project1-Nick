package com.github.Nick.reimbursements;

import java.io.Serializable;
import java.util.Objects;

public class ReimbResponse implements Serializable{
    
    private String reimb_id;
    private int amount;
    private String submitted;
    private String resolved;
    private String description;
    private String payment_id;
    private String author_id; // ? links to user_id
    private String resolver_id; // ? links to user_id
    private String status_id; // ? links to reimbursement statuses
    private String type_id; // ? links to reimbursement type

    public ReimbResponse (Reimb subject) {
        this.reimb_id = subject.getReimb_id();
        this.amount = subject.getAmount();
        this.submitted = subject.getSubmitted();
        this.resolved = subject.getResolved();
        this.description = subject.getDescription();
        this.payment_id = subject.getPayment_id();
        this.author_id = subject.getAuthor_id();
        this.resolver_id = subject.getResolver_id();
        this.status_id = subject.getResolver_id();
        this.type_id = subject.getType_id();
    }

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

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
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

    public String getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(String resolver_id) {
        this.resolver_id = resolver_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null ||getClass() != o.getClass()) {return false;}
        ReimbResponse reimbResponse = (ReimbResponse) o;
        return Objects.equals(reimb_id, reimbResponse.reimb_id) && Objects.equals(amount, reimbResponse.amount)
            && Objects.equals(submitted, reimbResponse.submitted) && Objects.equals(resolved, reimbResponse.resolved)
            && Objects.equals(description, reimbResponse.description) && Objects.equals(payment_id, reimbResponse.payment_id)
            && Objects.equals(author_id, reimbResponse.author_id) && Objects.equals(resolver_id, reimbResponse.resolver_id)
            && Objects.equals(status_id, reimbResponse.status_id) && Objects.equals(type_id, reimbResponse.type_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimb_id, amount, submitted, resolved, description, payment_id, author_id,
                            resolver_id, status_id, type_id);                   
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
                "status_id = '" + status_id + "' " +
                "type_id = '" + type_id + "'}";
    }
}
