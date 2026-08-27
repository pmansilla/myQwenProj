package com.czw.smartkit.bleModule.data;

import java.io.Serializable;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class MeasureData implements Serializable {
    private int avgHr;
    private int bdH;
    private int bdL;
    private int hr;
    private int maxHr;
    private int minHr;
    private int ox;

    public static MeasureData parserFromData(byte[] bArr) {
        MeasureData measureData = new MeasureData();
        measureData.setHr(BleUtil.byte2IntLR(bArr[1]));
        measureData.setBdH(BleUtil.byte2IntLR(bArr[2]));
        measureData.setBdL(BleUtil.byte2IntLR(bArr[3]));
        measureData.setOx(BleUtil.byte2IntLR(bArr[4]));
        measureData.setAvgHr(BleUtil.byte2IntLR(bArr[5]));
        measureData.setMaxHr(BleUtil.byte2IntLR(bArr[6]));
        measureData.setMinHr(BleUtil.byte2IntLR(bArr[7]));
        return measureData;
    }

    public int getAvgHr() {
        return this.avgHr;
    }

    public int getBdH() {
        return this.bdH;
    }

    public int getBdL() {
        return this.bdL;
    }

    public int getHr() {
        return this.hr;
    }

    public int getMaxHr() {
        return this.maxHr;
    }

    public int getMinHr() {
        return this.minHr;
    }

    public int getOx() {
        return this.ox;
    }

    public void setAvgHr(int i) {
        this.avgHr = i;
    }

    public void setBdH(int i) {
        this.bdH = i;
    }

    public void setBdL(int i) {
        this.bdL = i;
    }

    public void setHr(int i) {
        this.hr = i;
    }

    public void setMaxHr(int i) {
        this.maxHr = i;
    }

    public void setMinHr(int i) {
        this.minHr = i;
    }

    public void setOx(int i) {
        this.ox = i;
    }

    public String toString() {
        return "MeasureData{hr=" + this.hr + ", bdH=" + this.bdH + ", bdL=" + this.bdL + ", ox=" + this.ox + ", avgHr=" + this.avgHr + ", maxHr=" + this.maxHr + ", minHr=" + this.minHr + '}';
    }
}
