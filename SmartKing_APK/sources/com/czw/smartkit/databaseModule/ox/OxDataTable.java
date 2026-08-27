package com.czw.smartkit.databaseModule.ox;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;

/* loaded from: classes.dex */
public class OxDataTable implements Serializable {

    @PrimaryKey(AssignType.BY_MYSELF)
    private String dataId;
    private long date;
    private String dateTimeStr;

    @NotNull
    private boolean isSync;

    @NotNull
    private String number;

    @NotNull
    private String uid;

    public String getDataId() {
        return this.dataId;
    }

    public long getDate() {
        return this.date;
    }

    public String getDateTimeStr() {
        return this.dateTimeStr;
    }

    public String getNumber() {
        return TextUtils.isEmpty(this.number) ? AmapLoc.RESULT_TYPE_GPS : this.number;
    }

    public String getUid() {
        return this.uid;
    }

    public boolean isSync() {
        return this.isSync;
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

    public void setNumber(String str) {
        this.number = str;
    }

    public void setSync(boolean z) {
        this.isSync = z;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public String toString() {
        return "HRBean{dataId='" + this.dataId + "', date=" + this.date + ", uid='" + this.uid + "', number='" + this.number + "', isSync=" + this.isSync + ", dateTimeStr='" + this.dateTimeStr + "'}";
    }
}
