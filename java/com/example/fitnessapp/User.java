package com.example.fitnessapp;

public class User {
    private String email;
    private String password;
    private String fullName;


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public User(String email,String password,String fullName){
        this.email=email;
        this.password=password;
        this.fullName =fullName;
    }
}
