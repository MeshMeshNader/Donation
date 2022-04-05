package com.grad.donation.DataModels;

import java.util.ArrayList;

public class PostDataModel {

    String postKey;
    String postLabel;
    String postDescription;
    String postDonationValue;
    ArrayList<String> postImageURL;

    public PostDataModel() {
    }

    public PostDataModel(String postKey, String postLabel, String postDescription, String postDonationValue, ArrayList<String> postImageURL) {
        this.postKey = postKey;
        this.postLabel = postLabel;
        this.postDescription = postDescription;
        this.postDonationValue = postDonationValue;
        this.postImageURL = postImageURL;

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

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostDonationValue() {
        return postDonationValue;
    }

    public void setPostDonationValue(String postDonationValue) {
        this.postDonationValue = postDonationValue;
    }

    public ArrayList<String> getPostImageURL() {
        return postImageURL;
    }

    public void setPostImageURL(ArrayList<String> postImageURL) {
        this.postImageURL = postImageURL;
    }
}
