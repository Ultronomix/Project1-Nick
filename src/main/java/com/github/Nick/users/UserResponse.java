package com.github.Nick.users;

//response DTO
public class UserResponse {

    private String user_id;
    private String username;
    private String email;
    private String password;
    private String given_name;
    private String surname;
    private boolean is_active;
    private String role_id;

    public UserResponse (User subject) {
        this.user_id = subject.getUser_id();
        this.username = subject.getUsername();
        this.email = subject.getEmail();
        this.given_name = subject.getGiven_name();
        this.surname = subject.getSurname();
        this.is_active = subject.getIs_active();
        this.role_id = subject.getRole_id();
    }

    public void setUser_id (String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setGiven_name (String given_name) {
        this.given_name = given_name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setSurname (String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setIs_active (boolean is_active) {
        this.is_active =is_active;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setRole_id (String role_id) {
        this.role_id = role_id;
    }

    public String getRole_id() {
        return role_id;
    }

    @Override
    public String toString() {
        return  "User " + "{" +
                "user_id = '" + user_id +  "' " +
                "username = '" + username + "' " +
                "email = '" + email + "' " +
                "given_name = '" + given_name + "' " +
                "surname = '" + surname + "' " +
                "is_active = '" + is_active + "' " +
                "role_id = '" + role_id + "' " +
                "'}";
    }
    
}
