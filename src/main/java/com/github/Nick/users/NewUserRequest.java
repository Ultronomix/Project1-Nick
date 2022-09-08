package com.github.Nick.users;

import com.github.Nick.common.Request;

public class NewUserRequest implements Request<User>{

    private String user_id;
    private String username;
    private String email;
    private String password;
    private String given_name;
    private String surname;
    private boolean is_active;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGiven_name() {
        return this.given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isIs_active() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
    
    @Override
    public String toString() {
        return "NewUserRequest {" +
                "user_id = " + user_id + "' " +
                "username = " + username + "' " + 
                "given_name = " + is_active + "' " +
                "surname = " + surname + "' " +
                "password = " + password + "' " +
                "is_active" + is_active + "' " +
                "}";
    }

    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setUser_id(this.user_id);
        extractedEntity.setUsername(this.username);
        extractedEntity.setEmail(this.email);
        extractedEntity.setPassword(this.password);
        extractedEntity.setGiven_name(this.given_name);
        extractedEntity.setSurname(this.surname);
        extractedEntity.setIs_active(this.is_active);
        return extractedEntity;
    }
}
