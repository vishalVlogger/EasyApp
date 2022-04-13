package com.appdroid.ssbtproject.Holder;

import java.io.Serializable;
import java.util.Date;

public class DeliveryBoyHolder implements Serializable {
    String address,email,mobileNo,name,uidNo,uidPhoto,docId;
    Date date;

    public DeliveryBoyHolder(String address, String email, String mobileNo, String name, String uidNo, String uidPhoto, String docId, Date date) {
        this.address = address;
        this.email = email;
        this.mobileNo = mobileNo;
        this.name = name;
        this.uidNo = uidNo;
        this.uidPhoto = uidPhoto;
        this.docId = docId;
        this.date = date;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public DeliveryBoyHolder() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUidNo() {
        return uidNo;
    }

    public void setUidNo(String uidNo) {
        this.uidNo = uidNo;
    }

    public String getUidPhoto() {
        return uidPhoto;
    }

    public void setUidPhoto(String uidPhoto) {
        this.uidPhoto = uidPhoto;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
