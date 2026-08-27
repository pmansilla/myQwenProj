package com.czw.smartkit.netModule.entity;

import java.io.Serializable;

/* loaded from: classes.dex */
public class LocationEntity implements Serializable {
    private static final long serialVersionUID = -8230322426213150144L;
    private String city;
    private String cityCode;
    private String country;
    private double lat;
    private double lon;
    private String province;

    public String getCity() {
        return this.city;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public String getCountry() {
        return this.country;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public String getProvince() {
        return this.province;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCityCode(String str) {
        this.cityCode = str;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setLat(double d) {
        this.lat = d;
    }

    public void setLon(double d) {
        this.lon = d;
    }

    public void setProvince(String str) {
        this.province = str;
    }

    public String toString() {
        return "LocationEntity{country='" + this.country + "', province='" + this.province + "', city='" + this.city + "', cityCode='" + this.cityCode + "', lon=" + this.lon + ", lat=" + this.lat + '}';
    }
}
