package com.grad.donation.DataModels;

public class UserDataModel {
    String UserID;
    String Name;
    String email;
    String phoneNumber;
    String password;

    public UserDataModel() {
    }

    public UserDataModel(String userID, String Name, String email, String phoneNumber, String password) {
        UserID = userID;
        this.Name = Name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}




