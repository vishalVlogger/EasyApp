package com.appdroid.ssbtproject.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "card_products")
public class CardProductHolder implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int roomPostId;

    @ColumnInfo(name = "product_doc_id")
    private String product_doc_id;

    @ColumnInfo(name = "number_of_packs")
    private int number_of_packs;


    @ColumnInfo(name = "pack_id")
    private int pack_id;

    @ColumnInfo(name = "productPrice")
    private int productPrice;

    @ColumnInfo(name = "productMrpPrice")
    private int productMrpPrice;

    public CardProductHolder(int roomPostId, String product_doc_id, int number_of_packs, int pack_id, int productPrice, int productMrpPrice) {
        this.roomPostId = roomPostId;
        this.product_doc_id = product_doc_id;
        this.number_of_packs = number_of_packs;
        this.pack_id = pack_id;
        this.productPrice = productPrice;
        this.productMrpPrice = productMrpPrice;
    }

    public CardProductHolder() {
    }

    public int getRoomPostId() {
        return roomPostId;
    }

    public void setRoomPostId(int roomPostId) {
        this.roomPostId = roomPostId;
    }

    public String getProduct_doc_id() {
        return product_doc_id;
    }

    public void setProduct_doc_id(String product_doc_id) {
        this.product_doc_id = product_doc_id;
    }

    public int getNumber_of_packs() {
        return number_of_packs;
    }

    public void setNumber_of_packs(int number_of_packs) {
        this.number_of_packs = number_of_packs;
    }

    public int getPack_id() {
        return pack_id;
    }

    public void setPack_id(int pack_id) {
        this.pack_id = pack_id;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductMrpPrice() {
        return productMrpPrice;
    }

    public void setProductMrpPrice(int productMrpPrice) {
        this.productMrpPrice = productMrpPrice;
    }
}
