package com.grad.donation.DataModels;

public class MessageDataModel {
    String messageKey;
    String message;
    String Date;
    String userName;
    String userEmail;

    public MessageDataModel() {
    }

    public MessageDataModel(String messageKey, String message, String date, String userName, String userEmail) {
        this.messageKey = messageKey;
        this.message = message;
        Date = date;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
