package com.appdroid.ssbtproject.Notification;

public class Data {
    private String body,title,sent;
    private Integer icon;
    private String postid;

    public Data(String user, String body, String title, String sent, Integer icon, String flag, String postid) {

        this.body = body; // body aahe tyachi
        this.title = title; // title tyachi
        this.sent = sent; // jyala sent karaychay to
        this.icon = icon; // tyacha icon

        this.postid = postid;
    }



    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }


    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Data() {
    }




    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}
