package com.appdroid.ssbt_delivery_boy.holder;

import java.io.Serializable;

public class OrderUserHolder implements Serializable {
        String address,docID,email,name,no,pin,userId;

    public OrderUserHolder(String address, String docID, String email, String name, String no, String pin, String userId) {
        this.address = address;
        this.docID = docID;
        this.email = email;
        this.name = name;
        this.no = no;
        this.pin = pin;
        this.userId = userId;
    }

    public OrderUserHolder() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

