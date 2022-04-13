package com.appdroid.ssbtproject.Holder;

public class GroceryModel {
    Integer img_grocery;
    String txt_grocery;

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

    public GroceryModel(Integer img_grocery, String txt_grocery) {
        this.img_grocery = img_grocery;
        this.txt_grocery = txt_grocery;
    }
}
