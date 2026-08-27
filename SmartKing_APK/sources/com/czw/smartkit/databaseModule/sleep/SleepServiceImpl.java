package com.czw.smartkit.databaseModule.sleep;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.czw.utils.LogUtil;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SleepServiceImpl {
    private static SleepServiceImpl instance = new SleepServiceImpl();
    protected LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();
    private String tabName = SleepDataTable.class.getName().replaceAll("\\.", "_");

    private SleepServiceImpl() {
    }

    public static SleepServiceImpl getInstance() {
        return instance;
    }

    public void deleteUserAllData(String str) {
        this.liteOrm.delete(WhereBuilder.create(SleepDataTable.class).where("uid=?", str));
    }

    public SleepDataTable findLast(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(SleepDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE).limit(AmapLoc.RESULT_TYPE_WIFI_ONLY));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (SleepDataTable) query.get(0);
    }

    public SleepDataTable getDataByDay(String str, String str2) {
        LogUtil.e("debug==查询的日期=dateBean==>" + str2);
        ArrayList query = this.liteOrm.query(QueryBuilder.create(SleepDataTable.class).where("uid=? and date(startTime) = date(?)", str, str2));
        if (query == null || query.size() <= 0) {
            return null;
        }
        return (SleepDataTable) query.get(0);
    }

    public SleepDataTable getLastData(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(SleepDataTable.class).where("userId=?  ", str).appendOrderDescBy(IMAPStore.ID_DATE));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (SleepDataTable) query.get(0);
    }

    public String getLastDataDate(String str) {
        SleepDataTable sleepDataTable;
        ArrayList query = this.liteOrm.query(QueryBuilder.create(SleepDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE));
        if (query != null && query.size() > 0 && (sleepDataTable = (SleepDataTable) query.get(0)) != null) {
            String startTime = sleepDataTable.getStartTime();
            if (!TextUtils.isEmpty(startTime) && startTime.length() > 10) {
                return startTime.substring(0, 10);
            }
        }
        return "2018-01-01";
    }

    public List<SleepDataTable> getNotSyncData(String str) {
        return this.liteOrm.query(QueryBuilder.create(SleepDataTable.class).where("uid=? and isSync=?", str, false));
    }

    public void saveData(SleepDataTable sleepDataTable) {
        if (sleepDataTable == null || TextUtils.isEmpty(sleepDataTable.getUid()) || TextUtils.isEmpty(sleepDataTable.getStartTime())) {
            return;
        }
        sleepDataTable.setDataId(sleepDataTable.getStartTime() + "_" + sleepDataTable.getUid());
        this.liteOrm.save(sleepDataTable);
    }

    public void saveData(List<SleepDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        Iterator<SleepDataTable> it = list.iterator();
        while (it.hasNext()) {
            saveData(it.next());
        }
    }

    public void saveDaySleepEntity(SleepDataTable sleepDataTable) {
        if (sleepDataTable == null || TextUtils.isEmpty(sleepDataTable.getStartTime())) {
            return;
        }
        sleepDataTable.setDataId(sleepDataTable.getStartTime() + "_" + sleepDataTable.getUid());
        this.liteOrm.save(sleepDataTable);
    }
}
