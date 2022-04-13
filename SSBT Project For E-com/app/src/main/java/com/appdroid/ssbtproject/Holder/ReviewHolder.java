package com.appdroid.ssbtproject.Holder;

import java.util.Date;

public class ReviewHolder {
    String userId,review;
    Date date;
    float ratting;

    public ReviewHolder(String userId, String review, Date date, float ratting) {
        this.userId = userId;
        this.review = review;
        this.date = date;
        this.ratting = ratting;
    }

    public ReviewHolder() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getRatting() {
        return ratting;
    }

    public void setRatting(float ratting) {
        this.ratting = ratting;
    }
}
