package com.appdroid.ssbtproject.Notification;

public class Sender {

    private Data data;
    private String to;
    private String title;
    private String body;


    public Sender(Data data, String to, String title, String body) {
        this.data = data;
        this.to = to;
        this.title = title;
        this.body = body;
    }

    public Sender() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
