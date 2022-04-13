package com.appdroid.ssbt_delivery_boy.holder;



import java.io.Serializable;

public class OrderProductHolder implements Serializable {

    String productImage;

    String productPack;

    String productPrice;

    String productTitle;

    int productQty;

    String productDocId;


    public OrderProductHolder(String productImage, String productPack, String productPrice, String productTitle, int productQty, String productDocId) {

        this.productImage = productImage;
        this.productPack = productPack;
        this.productPrice = productPrice;
        this.productTitle = productTitle;
        this.productQty = productQty;
        this.productDocId = productDocId;
    }

    public OrderProductHolder() {
    }



    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPack() {
        return productPack;
    }

    public void setProductPack(String productPack) {
        this.productPack = productPack;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public String getProductDocId() {
        return productDocId;
    }

    public void setProductDocId(String productDocId) {
        this.productDocId = productDocId;
    }
}
