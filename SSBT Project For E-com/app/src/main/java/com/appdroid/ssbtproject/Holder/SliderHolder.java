package com.appdroid.ssbtproject.Holder;

import java.util.Date;

public class SliderHolder {
    String link,title,flag,docID,thumb;
    Date date;

    public SliderHolder(String link, String title, String flag, String docID, String thumb, Date date) {
        this.link = link;
        this.title = title;
        this.flag = flag;
        this.docID = docID;
        this.thumb = thumb;
        this.date = date;
    }

    public SliderHolder() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
