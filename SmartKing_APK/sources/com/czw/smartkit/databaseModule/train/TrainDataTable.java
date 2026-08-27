package com.czw.smartkit.databaseModule.train;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.litesuits.orm.db.enums.AssignType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TrainDataTable implements Serializable {
    private String calorie;
    private String dataDetail;

    @PrimaryKey(AssignType.BY_MYSELF)
    private String dataId;
    private String endTime;
    private String fasterRate;
    private boolean isSync = false;
    private String locationDetail;
    private String startTime;
    private String strength;
    private String timeConsuming;
    private String uid;

    public String getCalorie() {
        return this.calorie;
    }

    public String getDataDetail() {
        return TextUtils.isEmpty(this.dataDetail) ? AmapLoc.RESULT_TYPE_GPS : this.dataDetail;
    }

    public String getDataId() {
        return this.dataId;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getFasterRate() {
        return this.fasterRate;
    }

    public String getLocationDetail() {
        return this.locationDetail;
    }

    public ArrayList<double[]> getLocationPointers() {
        String replace = this.locationDetail.replace("\n", "").replace(SQLBuilder.BLANK, "");
        ArrayList<double[]> arrayList = new ArrayList<>();
        List list = (List) new Gson().fromJson(replace, new TypeToken<ArrayList<double[]>>() { // from class: com.czw.smartkit.databaseModule.train.TrainDataTable.1
        }.getType());
        if (list != null && list.size() > 0) {
            arrayList.addAll(list);
        }
        return arrayList;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getStrength() {
        return this.strength;
    }

    public String getTimeConsuming() {
        return this.timeConsuming;
    }

    public String getUid() {
        return this.uid;
    }

    public boolean isSync() {
        return this.isSync;
    }

    public void setCalorie(String str) {
        this.calorie = str;
    }

    public void setDataDetail(String str) {
        this.dataDetail = str;
    }

    public void setDataId(String str) {
        this.dataId = str;
    }

    public void setEndTime(String str) {
        this.endTime = str;
    }

    public void setFasterRate(String str) {
        this.fasterRate = str;
    }

    public void setLocationDetail(String str) {
        this.locationDetail = str;
    }

    public void setStartTime(String str) {
        this.startTime = str;
    }

    public void setStrength(String str) {
        this.strength = str;
    }

    public void setSync(boolean z) {
        this.isSync = z;
    }

    public void setTimeConsuming(String str) {
        this.timeConsuming = str;
    }

    public void setUid(String str) {
        this.uid = str;
    }

    public String toString() {
        return "TrainDataTable{dataId='" + this.dataId + "', uid='" + this.uid + "', startTime='" + this.startTime + "', endTime='" + this.endTime + "', timeConsuming='" + this.timeConsuming + "', strength='" + this.strength + "', calorie='" + this.calorie + "', fasterRate='" + this.fasterRate + "', dataDetail='" + this.dataDetail + "', locationDetail='" + this.locationDetail + "', isSync=" + this.isSync + '}';
    }
}
