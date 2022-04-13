package com.appdroid.ssbtproject.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "checkout_amount")
public class CheckOutAmount implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int roomPostId;

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "finalAmount")
    private int finalAmount;

    public CheckOutAmount(int roomPostId, String id, int finalAmount) {
        this.roomPostId = roomPostId;
        this.id = id;
        this.finalAmount = finalAmount;
    }

    public CheckOutAmount() {
    }

    public int getRoomPostId() {
        return roomPostId;
    }

    public void setRoomPostId(int roomPostId) {
        this.roomPostId = roomPostId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(int finalAmount) {
        this.finalAmount = finalAmount;
    }
}
