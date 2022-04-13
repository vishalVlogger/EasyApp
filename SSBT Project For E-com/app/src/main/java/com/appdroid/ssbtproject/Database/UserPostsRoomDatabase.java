package com.appdroid.ssbtproject.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.Room.SearchQueryRoomHolder;

@Database(entities = {CardProductHolder.class,CheckOutAmount.class, SearchQueryRoomHolder.class, OrderProductHolder.class},version = 1)
public abstract  class UserPostsRoomDatabase extends RoomDatabase {
    public abstract SaveProductToCardDao postsDao();

    private static volatile  UserPostsRoomDatabase INSTANCE;
    public static UserPostsRoomDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (UserPostsRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserPostsRoomDatabase.class,"database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}