package com.appdroid.ssbt_delivery_boy.holder;

public class Holder {

    String mobileNo;
    String name;
    String email;
    String uidPhoto;

    public Holder() {
    }

    public Holder(String mobileNo, String name, String email, String uidPhoto) {
        this.mobileNo = mobileNo;
        this.name = name;
        this.email = email;
        this.uidPhoto = uidPhoto;
    }

    public String getUidPhoto() {
        return uidPhoto;
    }

    public void setUidPhoto(String uidPhoto) {
        this.uidPhoto = uidPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
