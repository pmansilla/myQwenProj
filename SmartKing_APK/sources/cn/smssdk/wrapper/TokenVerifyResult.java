package cn.smssdk.wrapper;

import cn.smssdk.entity.BaseEntity;

/* loaded from: classes.dex */
public class TokenVerifyResult extends BaseEntity {
    private String opToken;
    private String operator;
    private String token;

    public TokenVerifyResult(String str, String str2, String str3) {
        this.opToken = str;
        this.token = str2;
        this.operator = str3;
    }

    public String getOpToken() {
        return this.opToken;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getToken() {
        return this.token;
    }

    public void setOpToken(String str) {
        this.opToken = str;
    }

    public void setOperator(String str) {
        this.operator = str;
    }

    public void setToken(String str) {
        this.token = str;
    }
}
