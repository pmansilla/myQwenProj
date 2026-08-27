package com.czw.smartkit.bleModule.step;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DevTotalStepBean implements Serializable {
    private String bleHexStr;
    private int calorie;
    private long date;
    private int distance;
    private int step;

    public String getBleHexStr() {
        return this.bleHexStr;
    }

    public int getCalorie() {
        return this.calorie;
    }

    public long getDate() {
        return this.date;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getStep() {
        return this.step;
    }

    public void setBleHexStr(String str) {
        this.bleHexStr = str;
    }

    public void setCalorie(int i) {
        this.calorie = i;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public void setDistance(int i) {
        this.distance = i;
    }

    public void setStep(int i) {
        this.step = i;
    }

    public String toString() {
        return "DevDayStepBean{calorie=" + this.calorie + ", distance=" + this.distance + ", step=" + this.step + ", date=" + this.date + ", bleHexStr='" + this.bleHexStr + "'}";
    }
}
