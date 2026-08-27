package com.czw.smartkit.bleModule.data;

import java.io.Serializable;

/* loaded from: classes.dex */
public class RemindSetting implements Serializable {
    public int hrCheckDuration;
    public boolean callEnable = true;
    public boolean dynamicHr = false;
    public int endSleepHour = 7;
    public boolean facebookEnable = false;
    public boolean hrCycle = false;
    public boolean kakaoEnable = false;
    public boolean lightScreen = false;
    public boolean lineEnable = false;
    public boolean linkedEnable = false;
    public boolean longSitEnable = false;
    public int longSitTime = 60;
    public boolean messageEnable = true;
    public boolean qqEnable = false;
    public boolean skypeEnable = false;
    public int startSleepHour = 22;
    public boolean twitterEnable = false;
    public boolean viberEnable = false;
    public boolean wechatEnable = false;
    public boolean whatappEnable = false;

    public String toString() {
        return "RemindSetting{callEnable=" + this.callEnable + ", dynamicHr=" + this.dynamicHr + ", endSleepHour=" + this.endSleepHour + ", facebookEnable=" + this.facebookEnable + ", hrCheckDuration=" + this.hrCheckDuration + ", hrCycle=" + this.hrCycle + ", kakaoEnable=" + this.kakaoEnable + ", lightScreen=" + this.lightScreen + ", lineEnable=" + this.lineEnable + ", linkedEnable=" + this.linkedEnable + ", longSitEnable=" + this.longSitEnable + ", longSitTime=" + this.longSitTime + ", messageEnable=" + this.messageEnable + ", qqEnable=" + this.qqEnable + ", skypeEnable=" + this.skypeEnable + ", startSleepHour=" + this.startSleepHour + ", twitterEnable=" + this.twitterEnable + ", viberEnable=" + this.viberEnable + ", wechatEnable=" + this.wechatEnable + ", whatappEnable=" + this.whatappEnable + '}';
    }
}
