package com.czw.smartkit.databaseModule.htxOta;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;

/* loaded from: classes.dex */
public class HtxOtaFailureEntity implements Serializable {
    private String deviceVersion;
    private String normalMac;
    private String otaFilePath;
    private String otaMac;

    @PrimaryKey(AssignType.BY_MYSELF)
    private String uid;

    public String getDeviceVersion() {
        return this.deviceVersion;
    }

    public String getNormalMac() {
        return this.normalMac;
    }

    public String getOtaFilePath() {
        return this.otaFilePath;
    }

    public String getOtaMac() {
        return this.otaMac;
    }

    public String getUid() {
        return this.uid;
    }

    public void setDeviceVersion(String str) {
        this.deviceVersion = str;
    }

    public void setNormalMac(String str) {
        this.normalMac = str;
    }

    public void setOtaFilePath(String str) {
        this.otaFilePath = str;
    }

    public void setOtaMac(String str) {
        this.otaMac = str;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public String toString() {
        return "HtxOtaFailureEntity{deviceVersion='" + this.deviceVersion + "', normalMac='" + this.normalMac + "', otaFilePath='" + this.otaFilePath + "', otaMac='" + this.otaMac + "', uid='" + this.uid + "'}";
    }
}
