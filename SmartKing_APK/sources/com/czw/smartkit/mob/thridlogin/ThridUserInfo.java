package com.czw.smartkit.mob.thridlogin;

/* loaded from: classes.dex */
public class ThridUserInfo {
    private String openId;
    private String sex;
    private String userIcon;
    private String userName;
    private String userNote;

    public String getOpenId() {
        return this.openId;
    }

    public String getSex() {
        return this.sex;
    }

    public String getUserIcon() {
        return this.userIcon;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserNote() {
        return this.userNote;
    }

    public void setOpenId(String str) {
        this.openId = str;
    }

    public void setSex(String str) {
        this.sex = str;
    }

    public void setUserIcon(String str) {
        this.userIcon = str;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public void setUserNote(String str) {
        this.userNote = str;
    }

    public String toString() {
        return "ThridUserInfo{openId='" + this.openId + "', userIcon='" + this.userIcon + "', userName='" + this.userName + "', sex='" + this.sex + "', userNote='" + this.userNote + "'}";
    }
}
