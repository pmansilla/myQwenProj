package com.czw.smartkit.databaseModule.blood;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;

/* loaded from: classes.dex */
public class BloodDataTable implements Serializable {

    @NotNull
    private String bpH;

    @NotNull
    private String bpL;

    @PrimaryKey(AssignType.BY_MYSELF)
    private String dataId;
    private long date;
    private String dateTimeStr;

    @NotNull
    private boolean isSync;

    @NotNull
    private String uid;

    public String getBpH() {
        return this.bpH;
    }

    public String getBpL() {
        return this.bpL;
    }

    public String getDataId() {
        return this.dataId;
    }

    public long getDate() {
        return this.date;
    }

    public String getDateTimeStr() {
        return this.dateTimeStr;
    }

    public String getUid() {
        return this.uid;
    }

    public boolean isSync() {
        return this.isSync;
    }

    public void setBpH(String str) {
        this.bpH = str;
    }

    public void setBpL(String str) {
        this.bpL = str;
    }

    public void setDataId(String str) {
        this.dataId = str;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public void setDateTimeStr(String str) {
        this.dateTimeStr = str;
    }

    public void setSync(boolean z) {
        this.isSync = z;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public String toString() {
        return "DevBloodEntity{dataId='" + this.dataId + "', date=" + this.date + ", uid='" + this.uid + "', bpH='" + this.bpH + "', bpL='" + this.bpL + "', isSync=" + this.isSync + ", dateTimeStr='" + this.dateTimeStr + "'}";
    }
}
