package com.luck.picture.lib.rxbus2;

/* loaded from: classes.dex */
public class BusData {
    String id;
    String status;

    public BusData() {
    }

    public BusData(String str, String str2) {
        this.id = str;
        this.status = str2;
    }

    public String getId() {
        return this.id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
