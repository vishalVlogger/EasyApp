package com.appdroid.ssbtproject.Holder;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "orderProductHolder")
public class OrderProductHolder implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int roomPostId;

    @ColumnInfo(name = "productImage")
    String productImage;

    @ColumnInfo(name = "productPack")
    String productPack;

    @ColumnInfo(name = "productPrice")
    String productPrice;

    @ColumnInfo(name = "productTitle")
    String productTitle;

    @ColumnInfo(name = "productQty")
    int productQty;

    @ColumnInfo(name = "productDocId")
    String productDocId;

    public OrderProductHolder(int roomPostId, String productImage, String productPack, String productPrice, String productTitle, int productQty, String productDocId) {
        this.roomPostId = roomPostId;
        this.productImage = productImage;
        this.productPack = productPack;
        this.productPrice = productPrice;
        this.productTitle = productTitle;
        this.productQty = productQty;
        this.productDocId = productDocId;
    }

    public OrderProductHolder() {
    }

    public int getRoomPostId() {
        return roomPostId;
    }

    public void setRoomPostId(int roomPostId) {
        this.roomPostId = roomPostId;
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
