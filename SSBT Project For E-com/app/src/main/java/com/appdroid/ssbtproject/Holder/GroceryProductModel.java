package com.appdroid.ssbtproject.Holder;

public class GroceryProductModel {
    Integer img_circle,img_grocery;
    String txt_grocery;

    public GroceryProductModel(Integer img_circle, Integer img_grocery, String txt_grocery) {
        this.img_circle = img_circle;
        this.img_grocery = img_grocery;
        this.txt_grocery = txt_grocery;
    }

    public Integer getImg_circle() {
        return img_circle;
    }

    public void setImg_circle(Integer img_circle) {
        this.img_circle = img_circle;
    }

    public Integer getImg_grocery() {
        return img_grocery;
    }

    public void setImg_grocery(Integer img_grocery) {
        this.img_grocery = img_grocery;
    }

    public String getTxt_grocery() {
        return txt_grocery;
    }

    public void setTxt_grocery(String txt_grocery) {
        this.txt_grocery = txt_grocery;
    }
}
