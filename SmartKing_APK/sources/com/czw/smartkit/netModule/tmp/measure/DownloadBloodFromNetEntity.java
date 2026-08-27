package com.czw.smartkit.netModule.tmp.measure;

import com.czw.smartkit.databaseModule.blood.BloodDataTable;
import com.czw.smartkit.user.UserUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes.dex */
public class DownloadBloodFromNetEntity implements Serializable {
    private static final String yyyyMMddHhmmssFormatString = "yyyy-MM-dd HH:mm:ss";
    private String datetime;
    private String xueya_di;
    private String xueya_gao;

    public static BloodDataTable getDevDayBloodEntity(DownloadBloodFromNetEntity downloadBloodFromNetEntity) {
        try {
            BloodDataTable bloodDataTable = new BloodDataTable();
            bloodDataTable.setUid(UserUtil.getUid());
            bloodDataTable.setDate(DateUtils.parseDate(downloadBloodFromNetEntity.getDatetime(), new String[]{yyyyMMddHhmmssFormatString}).getTime() / 1000);
            bloodDataTable.setDateTimeStr(downloadBloodFromNetEntity.getDatetime());
            bloodDataTable.setBpH(downloadBloodFromNetEntity.getXueya_gao());
            bloodDataTable.setBpL(downloadBloodFromNetEntity.getXueya_di());
            bloodDataTable.setSync(true);
            return bloodDataTable;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<BloodDataTable> getDevDayBloodEntity(List<DownloadBloodFromNetEntity> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() < 1) {
            return arrayList;
        }
        Iterator<DownloadBloodFromNetEntity> it = list.iterator();
        while (it.hasNext()) {
            BloodDataTable devDayBloodEntity = getDevDayBloodEntity(it.next());
            if (devDayBloodEntity != null) {
                arrayList.add(devDayBloodEntity);
            }
        }
        return arrayList;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public String getXueya_di() {
        return this.xueya_di;
    }

    public String getXueya_gao() {
        return this.xueya_gao;
    }

    public void setDatetime(String str) {
        this.datetime = str;
    }

    public void setXueya_di(String str) {
        this.xueya_di = str;
    }

    public void setXueya_gao(String str) {
        this.xueya_gao = str;
    }
}
