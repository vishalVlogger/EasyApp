package com.appdroid.ssbtproject.Holder;

import java.util.List;

public class DailyOrderHolder {
    List<OrderProductHolder> productsList;

    String userID,date;

    public DailyOrderHolder(List<OrderProductHolder> productsList, String date, String userID) {
        this.productsList = productsList;
        this.date = date;
        this.userID = userID;
    }

    public DailyOrderHolder() {
    }

    public List<OrderProductHolder> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<OrderProductHolder> productsList) {
        this.productsList = productsList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
