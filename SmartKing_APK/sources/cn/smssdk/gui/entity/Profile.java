package cn.smssdk.gui.entity;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Profile implements Serializable {
    private String avatar;
    private String country;
    private String nickName;
    private String phoneNum;
    private String uid;

    public Profile(String str, String str2, String str3, String str4, String str5) {
        this.nickName = str;
        this.phoneNum = str2;
        this.avatar = str3;
        this.country = str4;
        this.uid = str5;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getCountry() {
        return this.country;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public String getUid() {
        return this.uid;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setNickName(String str) {
        this.nickName = str;
    }

    public void setPhoneNum(String str) {
        this.phoneNum = str;
    }

    public void setUid(String str) {
        this.uid = str;
    }
}
