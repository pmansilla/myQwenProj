package com.czw.smartkit.netModule.tmp.measure;

import com.czw.smartkit.databaseModule.hr.HrDataTable;
import com.czw.smartkit.databaseModule.ox.OxDataTable;
import com.czw.smartkit.user.UserUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes.dex */
public class DownloadHrOxFromNetEntity implements Serializable {
    private static final String yyyyMMddHhmmssFormatString = "yyyy-MM-dd HH:mm:ss";
    private String datetime;
    private String number;

    public static HrDataTable getHrEntity(DownloadHrOxFromNetEntity downloadHrOxFromNetEntity) {
        try {
            HrDataTable hrDataTable = new HrDataTable();
            hrDataTable.setUid(UserUtil.getUid());
            hrDataTable.setDate(DateUtils.parseDate(downloadHrOxFromNetEntity.getDatetime(), new String[]{yyyyMMddHhmmssFormatString}).getTime() / 1000);
            hrDataTable.setDateTimeStr(downloadHrOxFromNetEntity.getDatetime());
            hrDataTable.setNumber(downloadHrOxFromNetEntity.getNumber());
            hrDataTable.setSync(true);
            return hrDataTable;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<HrDataTable> getHrEntity(List<DownloadHrOxFromNetEntity> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() < 1) {
            return arrayList;
        }
        Iterator<DownloadHrOxFromNetEntity> it = list.iterator();
        while (it.hasNext()) {
            HrDataTable hrEntity = getHrEntity(it.next());
            if (hrEntity != null) {
                arrayList.add(hrEntity);
            }
        }
        return arrayList;
    }

    public static OxDataTable getOxEntity(DownloadHrOxFromNetEntity downloadHrOxFromNetEntity) {
        try {
            OxDataTable oxDataTable = new OxDataTable();
            oxDataTable.setUid(UserUtil.getUid());
            oxDataTable.setDate(DateUtils.parseDate(downloadHrOxFromNetEntity.getDatetime(), new String[]{yyyyMMddHhmmssFormatString}).getTime() / 1000);
            oxDataTable.setDateTimeStr(downloadHrOxFromNetEntity.getDatetime());
            oxDataTable.setNumber(downloadHrOxFromNetEntity.getNumber());
            oxDataTable.setSync(true);
            return oxDataTable;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<OxDataTable> getOxEntity(List<DownloadHrOxFromNetEntity> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() < 1) {
            return arrayList;
        }
        Iterator<DownloadHrOxFromNetEntity> it = list.iterator();
        while (it.hasNext()) {
            OxDataTable oxEntity = getOxEntity(it.next());
            if (oxEntity != null) {
                arrayList.add(oxEntity);
            }
        }
        return arrayList;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public String getNumber() {
        return this.number;
    }

    public void setDatetime(String str) {
        this.datetime = str;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public String toString() {
        return "HrTmpEntity{datetime='" + this.datetime + "', number='" + this.number + "'}";
    }
}
