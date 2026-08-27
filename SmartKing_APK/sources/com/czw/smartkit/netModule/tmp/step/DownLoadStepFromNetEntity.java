package com.czw.smartkit.netModule.tmp.step;

import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.user.UserUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes.dex */
public class DownLoadStepFromNetEntity implements Serializable {
    private static final String yyyyMMddFormatString = "yyyy-MM-dd";
    private static final String yyyyMMddHHmmssFormatString = "yyyy-MM-dd HH:mm:ss";
    private String calorie;
    private String detailJson;
    private String distance = AmapLoc.RESULT_TYPE_GPS;
    private String goalWalk;
    private String startTime;
    private String timeConsuming;
    private String walkCounts;

    private static StepDataTable getStepEntity(DownLoadStepFromNetEntity downLoadStepFromNetEntity) {
        try {
            StepDataTable stepDataTable = new StepDataTable();
            stepDataTable.setUid(UserUtil.getUid());
            Date parseDate = DateUtils.parseDate(downLoadStepFromNetEntity.getStartTime(), new String[]{yyyyMMddHHmmssFormatString});
            stepDataTable.setDate(parseDate.getTime() / 1000);
            stepDataTable.setStartTime(DateFormatUtils.format(parseDate, yyyyMMddFormatString));
            stepDataTable.setSync(true);
            stepDataTable.setCalorie(Float.valueOf(downLoadStepFromNetEntity.getCalorie()).intValue());
            stepDataTable.setDistance(Float.valueOf(downLoadStepFromNetEntity.getDistance()).intValue());
            stepDataTable.setWalkCounts(Float.valueOf(downLoadStepFromNetEntity.getWalkCounts()).intValue());
            stepDataTable.setDetailJson(downLoadStepFromNetEntity.getDetailJson());
            return stepDataTable;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<StepDataTable> getStepEntity(List<DownLoadStepFromNetEntity> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() < 1) {
            return arrayList;
        }
        Iterator<DownLoadStepFromNetEntity> it = list.iterator();
        while (it.hasNext()) {
            StepDataTable stepEntity = getStepEntity(it.next());
            if (stepEntity != null) {
                arrayList.add(stepEntity);
            }
        }
        return arrayList;
    }

    public String getCalorie() {
        return this.calorie;
    }

    public String getDetailJson() {
        return this.detailJson;
    }

    public String getDistance() {
        return this.distance;
    }

    public String getGoalWalk() {
        return this.goalWalk;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getTimeConsuming() {
        return this.timeConsuming;
    }

    public String getWalkCounts() {
        return this.walkCounts;
    }

    public void setCalorie(String str) {
        this.calorie = str;
    }

    public void setDetailJson(String str) {
        this.detailJson = str;
    }

    public void setDistance(String str) {
        this.distance = str;
    }

    public void setGoalWalk(String str) {
        this.goalWalk = str;
    }

    public void setStartTime(String str) {
        this.startTime = str;
    }

    public void setTimeConsuming(String str) {
        this.timeConsuming = str;
    }

    public void setWalkCounts(String str) {
        this.walkCounts = str;
    }
}
