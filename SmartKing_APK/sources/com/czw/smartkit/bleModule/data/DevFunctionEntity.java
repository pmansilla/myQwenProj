package com.czw.smartkit.bleModule.data;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DevFunctionEntity implements Serializable {
    public static final int MTK = 3;
    public static final int hanTianXia = 2;
    public static final int nRF51822 = 1;
    public static final int nRF52832 = 2;
    public static final int nordic = 1;
    private int model;
    private int platform;
    public int branchNumber = 0;
    private boolean isSupportHr = true;
    private boolean isSupportBlood = true;
    private boolean isSupportOx = true;
    private boolean isSupportOTA = true;
    private boolean isSupportWeather = false;
    private int clockCount = 3;
    private boolean isSupportRemind = true;
    private boolean isSupportStep = true;
    private boolean isSupportSleep = true;

    public int getClockCount() {
        return this.clockCount;
    }

    public int getModel() {
        return this.model;
    }

    public int getPlatform() {
        return this.platform;
    }

    public boolean isSupportBlood() {
        return this.isSupportBlood;
    }

    public boolean isSupportHr() {
        return this.isSupportHr;
    }

    public boolean isSupportOTA() {
        return this.isSupportOTA;
    }

    public boolean isSupportOx() {
        return this.isSupportOx;
    }

    public boolean isSupportRemind() {
        return this.isSupportRemind;
    }

    public boolean isSupportSleep() {
        return this.isSupportSleep;
    }

    public boolean isSupportStep() {
        return this.isSupportStep;
    }

    public boolean isSupportWeather() {
        return this.isSupportWeather;
    }

    public void setClockCount(int i) {
        this.clockCount = i;
    }

    public void setModel(int i) {
        this.model = i;
    }

    public void setPlatform(int i) {
        this.platform = i;
    }

    public void setSupportBlood(boolean z) {
        this.isSupportBlood = z;
    }

    public void setSupportHr(boolean z) {
        this.isSupportHr = z;
    }

    public void setSupportOTA(boolean z) {
        this.isSupportOTA = z;
    }

    public void setSupportOx(boolean z) {
        this.isSupportOx = z;
    }

    public void setSupportRemind(boolean z) {
        this.isSupportRemind = z;
    }

    public void setSupportSleep(boolean z) {
        this.isSupportSleep = z;
    }

    public void setSupportStep(boolean z) {
        this.isSupportStep = z;
    }

    public void setSupportWeather(boolean z) {
        this.isSupportWeather = z;
    }

    public String toString() {
        return "DevFunctionEntity{branchNumber=" + this.branchNumber + ", platform=" + this.platform + ", model=" + this.model + ", isSupportHr=" + this.isSupportHr + ", isSupportBlood=" + this.isSupportBlood + ", isSupportOx=" + this.isSupportOx + ", isSupportOTA=" + this.isSupportOTA + ", isSupportWeather=" + this.isSupportWeather + ", clockCount=" + this.clockCount + ", isSupportRemind=" + this.isSupportRemind + ", isSupportStep=" + this.isSupportStep + ", isSupportSleep=" + this.isSupportSleep + '}';
    }
}
