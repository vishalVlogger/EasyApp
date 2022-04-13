package com.appdroid.ssbtproject.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.appdroid.ssbtproject.Holder.OrderProductHolder;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    UserPostsRoomDatabase roomDatabase;
    public ProductViewModel(@NonNull Application application) {
        super(application);
        roomDatabase = UserPostsRoomDatabase.getInstance(application.getApplicationContext());

    }
    public LiveData<List<CardProductHolder>> getCardsProductsCounts(){
        return roomDatabase.postsDao().getAllCardProductsListCounts();
    }
    public LiveData<List<OrderProductHolder>> getDailyOrdersProductsCounts(){
        return roomDatabase.postsDao().getAllDailyOrdersProductsListCounts();
    }

}
