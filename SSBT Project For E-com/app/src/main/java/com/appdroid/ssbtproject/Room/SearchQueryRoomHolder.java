package com.appdroid.ssbtproject.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "search")
public class SearchQueryRoomHolder implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int searchId;

    @ColumnInfo(name = "query")
    private String query;

    public SearchQueryRoomHolder(int searchId, String query) {
        this.searchId = searchId;
        this.query = query;
    }

    public SearchQueryRoomHolder() {
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
