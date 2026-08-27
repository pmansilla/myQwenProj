package com.czw.smartkit.bleModule.step;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DevPartStepBean implements Serializable {
    private String bleHexStr;
    private int calorie;
    private String dateStr;
    private String dateTimeStr;
    private int distanceM;
    private int hour;
    private int step;

    public String getBleHexStr() {
        return this.bleHexStr;
    }

    public int getCalorie() {
        return this.calorie;
    }

    public String getDateStr() {
        return this.dateStr;
    }

    public String getDateTimeStr() {
        return this.hour % 2 == 0 ? String.format("%s %02d:00:00", getDateStr(), Integer.valueOf(this.hour / 2)) : String.format("%s %02d:30:00", getDateStr(), Integer.valueOf((this.hour / 2) + 1));
    }

    public int getDistanceM() {
        return this.distanceM;
    }

    public int getHour() {
        return this.hour;
    }

    public String getHourRangeStr() {
        return this.hour % 2 == 0 ? String.format("%02d:00 ~ %02d:30", Integer.valueOf(this.hour / 2), Integer.valueOf(this.hour / 2)) : String.format("%02d:30 ~ %02d:00", Integer.valueOf(this.hour / 2), Integer.valueOf((this.hour / 2) + 1));
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

    public void setDateStr(String str) {
        this.dateStr = str;
    }

    public void setDateTimeStr(String str) {
        this.dateTimeStr = str;
    }

    public void setDistanceM(int i) {
        this.distanceM = i;
    }

    public void setHour(int i) {
        this.hour = i;
    }

    public void setStep(int i) {
        this.step = i;
    }

    public String toString() {
        return "DevPartStepBean{dateStr='" + this.dateStr + "', hour=" + this.hour + ", step=" + this.step + ", calorie=" + this.calorie + ", distanceM=" + this.distanceM + ", dateTimeStr='" + this.dateTimeStr + "', bleHexStr='" + this.bleHexStr + "'}";
    }
}
