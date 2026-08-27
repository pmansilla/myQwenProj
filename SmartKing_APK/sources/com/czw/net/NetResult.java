package com.czw.net;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;

/* loaded from: classes.dex */
public class NetResult<D> {
    private String allCalorie;
    private String allCounts;
    private String allTimeConsuming;
    private D data;
    private int errorCode = -1;
    private String msg = "";
    private String jsonString = "";

    public String getAllCalorie() {
        return TextUtils.isEmpty(this.allCalorie) ? AmapLoc.RESULT_TYPE_GPS : this.allCalorie;
    }

    public String getAllCounts() {
        return TextUtils.isEmpty(this.allCounts) ? AmapLoc.RESULT_TYPE_GPS : this.allCounts;
    }

    public String getAllTimeConsuming() {
        return this.allTimeConsuming;
    }

    public D getData() {
        return this.data;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getJsonString() {
        return this.jsonString;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setData(D d) {
        this.data = d;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }

    public void setJsonString(String str) {
        this.jsonString = str;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String toString() {
        return "NetResult{errorCode=" + this.errorCode + ", msg='" + this.msg + "', jsonString='" + this.jsonString + "', data=" + this.data + '}';
    }
}
