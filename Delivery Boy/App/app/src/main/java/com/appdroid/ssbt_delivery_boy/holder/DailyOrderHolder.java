package com.appdroid.ssbt_delivery_boy.holder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DailyOrderHolder implements Serializable {
    String  mobileNumber,date,status,userID,docId,deliveryBoyDocId,deliveryBoyName,deliveryBoyNo;
    List<OrderProductHolder> productsList;
    Date assignDate;

    public DailyOrderHolder(String mobileNumber, String date, String status, String userID, String docId, String deliveryBoyDocId, String deliveryBoyName, String deliveryBoyNo, List<OrderProductHolder> productsList, Date assignDate) {
        this.mobileNumber = mobileNumber;
        this.date = date;
        this.status = status;
        this.userID = userID;
        this.docId = docId;
        this.deliveryBoyDocId = deliveryBoyDocId;
        this.deliveryBoyName = deliveryBoyName;
        this.deliveryBoyNo = deliveryBoyNo;
        this.productsList = productsList;
        this.assignDate = assignDate;
    }

    public DailyOrderHolder() {
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDeliveryBoyDocId() {
        return deliveryBoyDocId;
    }

    public void setDeliveryBoyDocId(String deliveryBoyDocId) {
        this.deliveryBoyDocId = deliveryBoyDocId;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public void setDeliveryBoyName(String deliveryBoyName) {
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getDeliveryBoyNo() {
        return deliveryBoyNo;
    }

    public void setDeliveryBoyNo(String deliveryBoyNo) {
        this.deliveryBoyNo = deliveryBoyNo;
    }

    public List<OrderProductHolder> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<OrderProductHolder> productsList) {
        this.productsList = productsList;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }
}
