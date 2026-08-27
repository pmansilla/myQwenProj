package com.czw.smartkit.bleModule.measure;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DevMeasureBean implements Serializable {
    private int avgHr;
    private long dateTime;
    private int intBpH;
    private int intBpL;
    private int intHr;
    private int intOx;
    private int maxHr;
    private int minHr;

    public int getAvgHr() {
        return this.avgHr;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public int getIntBpH() {
        return this.intBpH;
    }

    public int getIntBpL() {
        return this.intBpL;
    }

    public int getIntHr() {
        return this.intHr;
    }

    public int getIntOx() {
        return this.intOx;
    }

    public int getMaxHr() {
        return this.maxHr;
    }

    public int getMinHr() {
        return this.minHr;
    }

    public void setAvgHr(int i) {
        this.avgHr = i;
    }

    public void setDateTime(long j) {
        this.dateTime = j;
    }

    public void setIntBpH(int i) {
        this.intBpH = i;
    }

    public void setIntBpL(int i) {
        this.intBpL = i;
    }

    public void setIntHr(int i) {
        this.intHr = i;
    }

    public void setIntOx(int i) {
        this.intOx = i;
    }

    public void setMaxHr(int i) {
        this.maxHr = i;
    }

    public void setMinHr(int i) {
        this.minHr = i;
    }

    public String toString() {
        return "DevRealMeasureEntity{dateTime=" + this.dateTime + ", intHr=" + this.intHr + ", intBpH=" + this.intBpH + ", intBpL=" + this.intBpL + ", intOx=" + this.intOx + ", avgHr=" + this.avgHr + ", maxHr=" + this.maxHr + ", minHr=" + this.minHr + '}';
    }
}
