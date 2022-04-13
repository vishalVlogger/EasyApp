package com.appdroid.ssbt_delivery_boy.holder;

import java.io.Serializable;

public class UserHolder implements Serializable {
    String address,name,no,pin,userId,docID,email;

    public UserHolder(String address, String name, String no, String pin, String userId, String docID, String email) {
        this.address = address;
        this.name = name;
        this.no = no;
        this.pin = pin;
        this.userId = userId;
        this.docID = docID;
        this.email = email;
    }

    public UserHolder() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}
