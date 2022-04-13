package com.appdroid.ssbtproject.Holder;

import java.util.Date;

public class LabelHolder {
    String flag,image,title;
    Date date;

    public LabelHolder(String flag, String image, String title, Date date) {
        this.flag = flag;
        this.image = image;
        this.title = title;
        this.date = date;
    }

    public LabelHolder() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
