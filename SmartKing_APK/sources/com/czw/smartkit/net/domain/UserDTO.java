package com.czw.smartkit.net.domain;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.io.Serializable;

/* loaded from: classes.dex */
public class UserDTO implements Serializable {
    private String accessToken;
    private String birthday;
    private String email;
    private String fromType;
    private String is_show;
    private String loginpwd;
    private String openId;
    private String phone;
    private String photo;
    private String reg_date;
    private String thirdPhoto;
    private String upd_time;
    private String userId = "000";
    private String walkGoal = "6000";
    private String nickname = "smartking";
    private String sex = AmapLoc.RESULT_TYPE_WIFI_ONLY;
    private String height = "170";
    private String weight = "60";
    private String age = "20";
    private String stepWidth = "70";

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getAge() {
        return this.age;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFromType() {
        return this.fromType;
    }

    public String getHeight() {
        return this.height;
    }

    public String getIs_show() {
        return this.is_show;
    }

    public String getLoginpwd() {
        return this.loginpwd;
    }

    public String getNickname() {
        this.nickname = TextUtils.isEmpty(this.nickname) ? "" : this.nickname;
        return this.nickname;
    }

    public String getOpenId() {
        return this.openId;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPhoto() {
        return TextUtils.isEmpty(this.photo) ? "" : this.photo;
    }

    public String getReg_date() {
        return this.reg_date;
    }

    public String getSex() {
        return this.sex;
    }

    public String getStepWidth() {
        return this.stepWidth;
    }

    public String getThirdPhoto() {
        return this.thirdPhoto;
    }

    public String getUpd_time() {
        return this.upd_time;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getWalkGoal() {
        return this.walkGoal;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setAccessToken(String str) {
        this.accessToken = str;
    }

    public void setAge(String str) {
        this.age = str;
    }

    public void setBirthday(String str) {
        this.birthday = str;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setFromType(String str) {
        this.fromType = str;
    }

    public void setHeight(String str) {
        this.height = str;
    }

    public void setIs_show(String str) {
        this.is_show = str;
    }

    public void setLoginpwd(String str) {
        this.loginpwd = str;
    }

    public void setNickname(String str) {
        this.nickname = str;
    }

    public void setOpenId(String str) {
        this.openId = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setPhoto(String str) {
        this.photo = str;
    }

    public void setReg_date(String str) {
        this.reg_date = str;
    }

    public void setSex(String str) {
        this.sex = str;
    }

    public void setStepWidth(String str) {
        this.stepWidth = str;
    }

    public void setThirdPhoto(String str) {
        this.thirdPhoto = str;
    }

    public void setUpd_time(String str) {
        this.upd_time = str;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public void setWalkGoal(String str) {
        this.walkGoal = str;
    }

    public void setWeight(String str) {
        this.weight = str;
    }

    public String toString() {
        return "UserDTO{userId='" + this.userId + "', loginpwd='" + this.loginpwd + "', walkGoal='" + this.walkGoal + "', nickname='" + this.nickname + "', sex='" + this.sex + "', photo='" + this.photo + "', height='" + this.height + "', weight='" + this.weight + "', email='" + this.email + "', phone='" + this.phone + "', birthday='" + this.birthday + "', fromType='" + this.fromType + "', openId='" + this.openId + "', accessToken='" + this.accessToken + "', age='" + this.age + "', thirdPhoto='" + this.thirdPhoto + "', is_show='" + this.is_show + "', reg_date='" + this.reg_date + "', upd_time='" + this.upd_time + "', stepWidth='" + this.stepWidth + "'}";
    }
}
