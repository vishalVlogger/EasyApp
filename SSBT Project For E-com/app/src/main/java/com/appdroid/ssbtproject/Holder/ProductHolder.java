package com.appdroid.ssbtproject.Holder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductHolder implements Serializable {
    String category,description,image,price,subCategory,title,unit,docId,homeVisible,visibility,similarTags;
    Date date;
    List<PackHolder> sellUnit;
    List<String> suggestTags;
    boolean dailyDeliver;
    int discountPer,packPrice;
    String packMrp;
    Long quntity;

    public ProductHolder() {
    }

    public ProductHolder(String category, String description, String image, String price, String subCategory, String title, String unit, String docId, String homeVisible, String visibility, String similarTags, Date date, List<PackHolder> sellUnit, List<String> suggestTags, boolean dailyDeliver, int discountPer, int packPrice, String packMrp, Long quntity) {
        this.category = category;
        this.description = description;
        this.image = image;
        this.price = price;
        this.subCategory = subCategory;
        this.title = title;
        this.unit = unit;
        this.docId = docId;
        this.homeVisible = homeVisible;
        this.visibility = visibility;
        this.similarTags = similarTags;
        this.date = date;
        this.sellUnit = sellUnit;
        this.suggestTags = suggestTags;
        this.dailyDeliver = dailyDeliver;
        this.discountPer = discountPer;
        this.packPrice = packPrice;
        this.packMrp = packMrp;
        this.quntity = quntity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getHomeVisible() {
        return homeVisible;
    }

    public void setHomeVisible(String homeVisible) {
        this.homeVisible = homeVisible;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getSimilarTags() {
        return similarTags;
    }

    public void setSimilarTags(String similarTags) {
        this.similarTags = similarTags;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<PackHolder> getSellUnit() {
        return sellUnit;
    }

    public void setSellUnit(List<PackHolder> sellUnit) {
        this.sellUnit = sellUnit;
    }

    public List<String> getSuggestTags() {
        return suggestTags;
    }

    public void setSuggestTags(List<String> suggestTags) {
        this.suggestTags = suggestTags;
    }

    public boolean isDailyDeliver() {
        return dailyDeliver;
    }

    public void setDailyDeliver(boolean dailyDeliver) {
        this.dailyDeliver = dailyDeliver;
    }

    public int getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public int getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(int packPrice) {
        this.packPrice = packPrice;
    }

    public String getPackMrp() {
        return packMrp;
    }

    public void setPackMrp(String packMrp) {
        this.packMrp = packMrp;
    }

    public Long getQuntity() {
        return quntity;
    }

    public void setQuntity(Long quntity) {
        this.quntity = quntity;
    }
}
