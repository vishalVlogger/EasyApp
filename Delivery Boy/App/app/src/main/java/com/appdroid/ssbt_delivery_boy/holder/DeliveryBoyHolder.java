package com.appdroid.ssbt_delivery_boy.holder;

import java.util.Date;

public class DeliveryBoyHolder {

    String deliveryBoy,orderDocId;
    Date date;

    public DeliveryBoyHolder(String deliveryBoy, String orderDocId, Date date) {
        this.deliveryBoy = deliveryBoy;
        this.orderDocId = orderDocId;
        this.date = date;
    }

    public DeliveryBoyHolder() {
    }

    public String getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(String deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public String getOrderDocId() {
        return orderDocId;
    }

    public void setOrderDocId(String orderDocId) {
        this.orderDocId = orderDocId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
