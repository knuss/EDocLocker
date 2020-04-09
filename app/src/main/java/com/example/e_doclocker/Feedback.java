package com.example.e_doclocker;

public class Feedback {
    String userID,feedback;

    public Feedback(String userID, String feedback) {
        this.userID = userID;
        this.feedback = feedback;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return feedback+"\nBy: "+userID;
    }
}
