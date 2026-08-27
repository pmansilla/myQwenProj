package com.czw.smartkit.bleModule.sleep;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DevPartSleepBean implements Serializable {
    private String bleHexString;
    private long date;
    private int duration;
    private int sleepType;

    public String getBleHexString() {
        return this.bleHexString;
    }

    public long getDate() {
        return this.date;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getSleepType() {
        return this.sleepType;
    }

    public void setBleHexString(String str) {
        this.bleHexString = str;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public void setSleepType(int i) {
        this.sleepType = i;
    }

    public String toString() {
        return "DevMinuteSleepBean{sleepType=" + this.sleepType + ", duration=" + this.duration + ", date=" + this.date + ", bleHexString='" + this.bleHexString + "'}";
    }
}
