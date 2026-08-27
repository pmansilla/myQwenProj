package com.czw.smartkit.databaseModule.sleep;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;

/* loaded from: classes.dex */
public class SleepDataTable implements Serializable {

    @PrimaryKey(AssignType.BY_MYSELF)
    private String dataId;
    private long date;
    private String endTime;
    private boolean isSync;
    private String record;
    private String startTime;
    private int totalTime;
    private String uid;
    private int deepTime = 0;
    private int shallowTime = 0;
    private int soberTime = 0;

    public String getDataId() {
        return this.dataId;
    }

    public long getDate() {
        return this.date;
    }

    public int getDeepTime() {
        return this.deepTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getRecord() {
        return this.record;
    }

    public int getShallowTime() {
        return this.shallowTime;
    }

    public int getSoberTime() {
        return this.soberTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public int getTotalTime() {
        return this.totalTime;
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

    public void setDeepTime(int i) {
        this.deepTime = i;
    }

    public void setEndTime(String str) {
        this.endTime = str;
    }

    public void setRecord(String str) {
        this.record = str;
    }

    public void setShallowTime(int i) {
        this.shallowTime = i;
    }

    public void setSoberTime(int i) {
        this.soberTime = i;
    }

    public void setStartTime(String str) {
        this.startTime = str;
    }

    public void setSync(boolean z) {
        this.isSync = z;
    }

    public void setTotalTime(int i) {
        this.totalTime = i;
    }

    public void setUid(String str) {
        this.uid = str;
    }
}
