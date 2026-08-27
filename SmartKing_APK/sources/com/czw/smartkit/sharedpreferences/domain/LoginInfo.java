package com.czw.smartkit.sharedpreferences.domain;

import android.text.TextUtils;
import java.io.Serializable;

/* loaded from: classes.dex */
public class LoginInfo implements Serializable {
    private String name;
    private String value;

    public LoginInfo(String str, String str2) {
        this.name = str;
        this.value = str2;
    }

    public String getName() {
        return TextUtils.isEmpty(this.name) ? "" : this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setValue(String str) {
        this.value = str;
    }
}
