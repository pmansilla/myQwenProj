package com.czw.smartkit.bleModule.sleep;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DevTotalSleepBean implements Serializable {
    private String bleHexStr;
    private long date;
    private int totalDeep = 0;
    private int totalLight = 0;
    private int totalAwake = 0;

    public String getBleHexStr() {
        return this.bleHexStr;
    }

    public long getDate() {
        return this.date;
    }

    public int getSleep() {
        return this.totalDeep + this.totalLight;
    }

    public int getTotal() {
        return getTotalAwake() + getSleep();
    }

    public int getTotalAwake() {
        return this.totalAwake;
    }

    public int getTotalDeep() {
        return this.totalDeep;
    }

    public int getTotalLight() {
        return this.totalLight;
    }

    public void setBleHexStr(String str) {
        this.bleHexStr = str;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public void setTotalAwake(int i) {
        this.totalAwake = i;
    }

    public void setTotalDeep(int i) {
        this.totalDeep = i;
    }

    public void setTotalLight(int i) {
        this.totalLight = i;
    }

    public String toString() {
        return "DevDaySleepBean{bleHexStr='" + this.bleHexStr + "', totalDeep=" + this.totalDeep + ", totalLight=" + this.totalLight + ", totalAwake=" + this.totalAwake + ", date=" + this.date + '}';
    }
}
