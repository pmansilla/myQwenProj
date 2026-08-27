package com.czw.smartkit.netModule.tmp.sleep;

import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.user.UserUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes.dex */
public class DownloadSleepFromNetEntity implements Serializable {
    private static final String yyyyMMddFormatString = "yyyy-MM-dd";
    private static final String yyyyMMddHHmmssFormatString = "yyyy-MM-dd HH:mm:ss";
    private String endTime;
    private String record;
    private String startTime;
    private int totalTime;
    private int deepTime = 0;
    private int shallowTime = 0;
    private int soberTime = 0;

    private static SleepDataTable getSleepEntity(DownloadSleepFromNetEntity downloadSleepFromNetEntity) {
        try {
            SleepDataTable sleepDataTable = new SleepDataTable();
            sleepDataTable.setUid(UserUtil.getUid());
            Date parseDate = DateUtils.parseDate(downloadSleepFromNetEntity.getStartTime(), new String[]{yyyyMMddHHmmssFormatString});
            sleepDataTable.setDate(parseDate.getTime() / 1000);
            sleepDataTable.setStartTime(DateFormatUtils.format(parseDate, yyyyMMddHHmmssFormatString));
            sleepDataTable.setEndTime(DateFormatUtils.format(parseDate, yyyyMMddHHmmssFormatString));
            sleepDataTable.setTotalTime(Integer.valueOf(downloadSleepFromNetEntity.getTotalTime()).intValue());
            sleepDataTable.setDeepTime(Integer.valueOf(downloadSleepFromNetEntity.getDeepTime()).intValue());
            sleepDataTable.setShallowTime(Integer.valueOf(downloadSleepFromNetEntity.getShallowTime()).intValue());
            sleepDataTable.setSoberTime(Integer.valueOf(downloadSleepFromNetEntity.getSoberTime()).intValue());
            sleepDataTable.setSync(true);
            sleepDataTable.setRecord(downloadSleepFromNetEntity.getRecord());
            return sleepDataTable;
        } catch (Exception unused) {
            return null;
        }
    }

    public static List<SleepDataTable> getSleepEntity(List<DownloadSleepFromNetEntity> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() < 1) {
            return arrayList;
        }
        Iterator<DownloadSleepFromNetEntity> it = list.iterator();
        while (it.hasNext()) {
            SleepDataTable sleepEntity = getSleepEntity(it.next());
            if (sleepEntity != null) {
                arrayList.add(sleepEntity);
            }
        }
        return arrayList;
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

    public void setTotalTime(int i) {
        this.totalTime = i;
    }
}
