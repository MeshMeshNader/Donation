package com.grad.donation.DataModels;

public class RequestDataModel {
    String requestKey;
    String userID;
    String userName;
    String postKey;
    String postLabel;
    String status;

    public RequestDataModel() {
    }

    public RequestDataModel(String requestKey, String userID, String userName, String postKey, String postLabel, String status) {
        this.requestKey = requestKey;
        this.userID = userID;
        this.userName = userName;
        this.postKey = postKey;
        this.postLabel = postLabel;
        this.status = status;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPostLabel() {
        return postLabel;
    }

    public void setPostLabel(String postLabel) {
        this.postLabel = postLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
