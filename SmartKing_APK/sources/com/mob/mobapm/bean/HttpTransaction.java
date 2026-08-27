package com.mob.mobapm.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class HttpTransaction implements Serializable {
    private static final long serialVersionUID = 1;
    private String body;
    private long clientTime;
    private String dataNetworkType;
    private String duid;
    private String host;
    private String method;
    private String networkType;
    private String path;
    private String query;
    private long requestDuration;
    private long requestTime;
    private long responseTime;

    public String getBody() {
        return this.body;
    }

    public long getClientTime() {
        return this.clientTime;
    }

    public String getDataNetworkType() {
        return this.dataNetworkType;
    }

    public String getDuid() {
        return this.duid;
    }

    public String getHost() {
        return this.host;
    }

    public String getMethod() {
        return this.method;
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public String getPath() {
        return this.path;
    }

    public String getQuery() {
        return this.query;
    }

    public long getRequestDuration() {
        return this.requestDuration;
    }

    public long getRequestTime() {
        return this.requestTime;
    }

    public long getResponseTime() {
        return this.responseTime;
    }

    public void setBody(String str) {
        this.body = str;
    }

    public void setClientTime(long j) {
        this.clientTime = j;
    }

    public void setDataNetworkType(String str) {
        this.dataNetworkType = str;
    }

    public void setDuid(String str) {
        this.duid = str;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public void setNetworkType(String str) {
        this.networkType = str;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setQuery(String str) {
        this.query = str;
    }

    public void setRequestDuration(long j) {
        this.requestDuration = j;
    }

    public void setRequestTime(long j) {
        this.requestTime = j;
    }

    public void setResponseTime(long j) {
        this.responseTime = j;
    }

    public String toString() {
        return "{host:" + this.host + ",path:" + this.path + ",method:" + this.method + ",requestTime:" + this.requestTime + ",clientTime:" + this.clientTime + ",responseTime:" + this.responseTime + ",requestDuration:" + this.requestDuration + ",networkType:" + this.networkType + ",dataNetworkType:" + this.dataNetworkType + "}";
    }
}
