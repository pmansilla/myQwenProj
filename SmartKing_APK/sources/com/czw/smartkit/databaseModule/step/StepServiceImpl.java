package com.czw.smartkit.databaseModule.step;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.sun.mail.imap.IMAPStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class StepServiceImpl {
    private static StepServiceImpl sportDao = new StepServiceImpl();
    private static final SimpleDateFormat yyyyMMddSmp = new SimpleDateFormat("yyyy-MM-dd");
    protected LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();
    String tabName = StepDataTable.class.getName().replaceAll("\\.", "_");

    private StepServiceImpl() {
    }

    public static StepServiceImpl getInstance() {
        return sportDao;
    }

    public void deleteDayData(String str, String str2) {
        this.liteOrm.delete(WhereBuilder.create(StepDataTable.class).where("uid=? and date(startTime)=date(?)", str, str2));
    }

    public void deleteUserAllData(String str) {
        this.liteOrm.delete(WhereBuilder.create(StepDataTable.class).where("uid=?", str));
    }

    public StepDataTable findLast(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(StepDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE).limit(AmapLoc.RESULT_TYPE_WIFI_ONLY));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (StepDataTable) query.get(0);
    }

    public String getLastDataDate(String str) {
        StepDataTable stepDataTable;
        ArrayList query = this.liteOrm.query(QueryBuilder.create(StepDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE));
        if (query != null && query.size() > 0 && (stepDataTable = (StepDataTable) query.get(0)) != null) {
            String startTime = stepDataTable.getStartTime();
            if (!TextUtils.isEmpty(startTime) && startTime.length() > 10) {
                return startTime.substring(0, 10);
            }
        }
        return "2018-01-01";
    }

    public List<StepDataTable> getNotSyncData(String str) {
        return this.liteOrm.query(QueryBuilder.create(StepDataTable.class).where("uid=? and isSync=?", str, false));
    }

    public StepDataTable getStepDataByDate(String str, String str2) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(StepDataTable.class).where("uid=? and date(startTime)=date(?)", str, str2));
        if (query == null || query.size() <= 0) {
            return null;
        }
        return (StepDataTable) query.get(0);
    }

    public void saveData(StepDataTable stepDataTable) {
        if (stepDataTable == null || TextUtils.isEmpty(stepDataTable.getUid()) || TextUtils.isEmpty(stepDataTable.getStartTime())) {
            return;
        }
        LogUtil.e("db-步数/保存的数据是:" + new Gson().toJson(stepDataTable));
        stepDataTable.setDataId(stepDataTable.getStartTime() + "_" + stepDataTable.getUid());
        this.liteOrm.save(stepDataTable);
    }

    public void saveData(List<StepDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        Iterator<StepDataTable> it = list.iterator();
        while (it.hasNext()) {
            saveData(it.next());
        }
    }
}
