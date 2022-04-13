package com.appdroid.ssbt_delivery_boy.holder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderHolder implements Serializable {
    Date orderDate,assignDate;
    String orderId,status,userNo,docID,deliveryBoyDocId,deliveryBoyName,deliveryBoyNo,cancellationReason,orderCancelBy;
    int orderTotal;
    List<OrderProductHolder> productDetails;
    OrderUserHolder userInfo;


    public OrderHolder(Date orderDate, Date assignDate, String orderId, String status, String userNo, String docID, String deliveryBoyDocId, String deliveryBoyName, String deliveryBoyNo, String cancellationReason, String orderCancelBy, int orderTotal, List<OrderProductHolder> productDetails, OrderUserHolder userInfo) {
        this.orderDate = orderDate;
        this.assignDate = assignDate;
        this.orderId = orderId;
        this.status = status;
        this.userNo = userNo;
        this.docID = docID;
        this.deliveryBoyDocId = deliveryBoyDocId;
        this.deliveryBoyName = deliveryBoyName;
        this.deliveryBoyNo = deliveryBoyNo;
        this.cancellationReason = cancellationReason;
        this.orderCancelBy = orderCancelBy;
        this.orderTotal = orderTotal;
        this.productDetails = productDetails;
        this.userInfo = userInfo;
    }

    public OrderHolder() {
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getOrderCancelBy() {
        return orderCancelBy;
    }

    public void setOrderCancelBy(String orderCancelBy) {
        this.orderCancelBy = orderCancelBy;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<OrderProductHolder> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<OrderProductHolder> productDetails) {
        this.productDetails = productDetails;
    }

    public OrderUserHolder getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(OrderUserHolder userInfo) {
        this.userInfo = userInfo;
    }
}
