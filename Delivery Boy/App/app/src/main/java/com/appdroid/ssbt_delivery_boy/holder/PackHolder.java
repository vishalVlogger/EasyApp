package com.appdroid.ssbt_delivery_boy.holder;

import java.io.Serializable;

public class PackHolder implements Serializable {
    String pack,price,mrp;

    public PackHolder(String pack, String price, String mrp) {
        this.pack = pack;
        this.price = price;
        this.mrp = mrp;
    }

    public PackHolder() {
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
