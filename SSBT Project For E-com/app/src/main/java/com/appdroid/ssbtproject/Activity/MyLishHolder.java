package com.appdroid.ssbtproject.Activity;

public class MyLishHolder {

    String docId,pName;

    public MyLishHolder(String docId, String pName) {
        this.docId = docId;
        this.pName = pName;
    }

    public MyLishHolder() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
