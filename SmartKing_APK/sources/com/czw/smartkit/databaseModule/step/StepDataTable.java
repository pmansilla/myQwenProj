package com.czw.smartkit.databaseModule.step;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;

/* loaded from: classes.dex */
public class StepDataTable implements Serializable {
    private int calorie;

    @PrimaryKey(AssignType.BY_MYSELF)
    private String dataId;
    private long date;
    private String detailJson;
    private int distance;
    private int goalWalk;
    private boolean isSync;
    private String startTime;
    private int timeConsuming;
    private String uid;
    private int walkCounts;

    public int getCalorie() {
        return this.calorie;
    }

    public String getDataId() {
        return this.dataId;
    }

    public long getDate() {
        return this.date;
    }

    public String getDetailJson() {
        return this.detailJson;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getGoalWalk() {
        return this.goalWalk;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public int getTimeConsuming() {
        return this.timeConsuming;
    }

    public String getUid() {
        return this.uid;
    }

    public int getWalkCounts() {
        return this.walkCounts;
    }

    public boolean isSync() {
        return this.isSync;
    }

    public void setCalorie(int i) {
        this.calorie = i;
    }

    public void setDataId(String str) {
        this.dataId = str;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public void setDetailJson(String str) {
        this.detailJson = str;
    }

    public void setDistance(int i) {
        this.distance = i;
    }

    public void setGoalWalk(int i) {
        this.goalWalk = i;
    }

    public void setStartTime(String str) {
        this.startTime = str;
    }

    public void setSync(boolean z) {
        this.isSync = z;
    }

    public void setTimeConsuming(int i) {
        this.timeConsuming = i;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public void setWalkCounts(int i) {
        this.walkCounts = i;
    }
}
