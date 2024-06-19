package com.example.securityallocator.models;

public class UserModel {
    private String userUID;
    public String username;
    public String email;
    public String role;
    private String firstname;
    private String lastname;
    private String gender;
    private String contact;
    private String address;


    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserModel(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public UserModel(String userUID, String username, String email, String role, String firstname, String lastname, String gender, String contact, String address) {
        this.userUID = userUID;
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }


    public UserModel(String username, String firstname, String lastname, String gender, String contact, String address) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
