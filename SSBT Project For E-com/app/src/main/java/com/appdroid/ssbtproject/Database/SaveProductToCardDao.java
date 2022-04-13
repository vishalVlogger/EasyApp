package com.appdroid.ssbtproject.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.appdroid.ssbtproject.Holder.OrderProductHolder;
import com.appdroid.ssbtproject.Room.SearchQueryRoomHolder;

import java.util.List;
@Dao
public interface SaveProductToCardDao {

    @Insert
    void addProductToCard(CardProductHolder product);

    @Query("UPDATE card_products SET number_of_packs =:numberOfPacks WHERE product_doc_id = :productDocId")
    void updateProductNumbers(int numberOfPacks, String productDocId);

    @Query("SELECT * FROM card_products")
    List<CardProductHolder> getAllCardProductsList();

    @Query("SELECT * FROM card_products WHERE product_doc_id = :productDocId")
    CardProductHolder findByProductId(String productDocId);


    @Query("SELECT EXISTS (SELECT 1 FROM card_products WHERE product_doc_id = :productDocId)")
    boolean isProductAvailble(String productDocId);

    @Delete
    void deleteProductFromCard(CardProductHolder productHolder);

    @Query("SELECT EXISTS (SELECT 1 FROM checkout_amount WHERE id = :checkId)")
    boolean isCheckOutPriceAvailble(String checkId);

    @Insert
    void addCheckOutValue(CheckOutAmount checkOutAmount);

    @Query("UPDATE checkout_amount SET finalAmount = finalAmount + :updatedValue WHERE id =:updateID")
    void updateCheckOutValue(int updatedValue, String updateID);

    @Query("SELECT * FROM checkout_amount WHERE id = :updateID")
    CheckOutAmount findCheckOutValue(String updateID);

    @Query("DELETE FROM card_products")
    void clearCard();

    @Query("SELECT * FROM card_products")
    LiveData<List<CardProductHolder>> getAllCardProductsListCounts();

    @Query("SELECT * FROM orderProductHolder")
    LiveData<List<OrderProductHolder>> getAllDailyOrdersProductsListCounts();

    @Insert
    void insertSearchItem(SearchQueryRoomHolder posts);

    @Query("SELECT * FROM search")
    List<SearchQueryRoomHolder> getAllSearchHistory();

    @Query("DELETE FROM search")
    void clearAllHistory();


    @Insert
    void addDailyOrderProduct(OrderProductHolder product);

    @Query("SELECT EXISTS (SELECT 1 FROM orderProductHolder WHERE productTitle = :productDocId)")
    boolean isDailyOrderProductAllredyAddedToCard(String productDocId);

    @Delete
    void deleteDailyOrderProduct(OrderProductHolder product);

    @Query("SELECT * FROM orderProductHolder WHERE productTitle = :productDocId")
    OrderProductHolder findByDailyOrderProductName(String productDocId);
}


