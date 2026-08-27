package com.czw.smartkit.databaseModule.blood;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.sun.mail.imap.IMAPStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class BloodServiceImpl {
    private static BloodServiceImpl bloodDao = new BloodServiceImpl();
    protected LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();
    private SimpleDateFormat MMddSmp = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat HHmmSmp = new SimpleDateFormat("HH:mm");
    String tabName = BloodDataTable.class.getName().replaceAll("\\.", "_");

    private BloodServiceImpl() {
    }

    public static BloodServiceImpl getInstance() {
        return bloodDao;
    }

    public void deleteUserAllData(String str) {
        this.liteOrm.delete(WhereBuilder.create(BloodDataTable.class).where("uid=?", str));
    }

    public BloodDataTable findLast(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(BloodDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE).limit(AmapLoc.RESULT_TYPE_WIFI_ONLY));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (BloodDataTable) query.get(0);
    }

    public List<BloodDataTable> getData(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(BloodDataTable.class).where("uid=? and date(dateTimeStr)==date(?) ", str, str2).appendOrderAscBy(IMAPStore.ID_DATE));
    }

    public List<BloodDataTable> getDataByDesc(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(BloodDataTable.class).where("uid=? and date(dateTimeStr)==date(?) ", str, str2).appendOrderDescBy(IMAPStore.ID_DATE));
    }

    public String getLastDataDate(String str) {
        BloodDataTable bloodDataTable;
        ArrayList query = this.liteOrm.query(QueryBuilder.create(BloodDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE));
        return (query == null || query.size() <= 0 || (bloodDataTable = (BloodDataTable) query.get(0)) == null || TextUtils.isEmpty(bloodDataTable.getDateTimeStr()) || bloodDataTable.getDateTimeStr().length() <= 10) ? "2018-09-09" : bloodDataTable.getDateTimeStr().substring(0, 10);
    }

    public List<BloodDataTable> getNotSyncData(String str) {
        return this.liteOrm.query(QueryBuilder.create(BloodDataTable.class).where("uid=? and isSync=?", str, false));
    }

    public void save(BloodDataTable bloodDataTable) {
        if (bloodDataTable == null || TextUtils.isEmpty(bloodDataTable.getUid()) || TextUtils.isEmpty(bloodDataTable.getDateTimeStr())) {
            return;
        }
        bloodDataTable.setDataId(bloodDataTable.getDateTimeStr() + "_" + bloodDataTable.getUid());
        this.liteOrm.save(bloodDataTable);
    }

    public void saveData(List<BloodDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        Iterator<BloodDataTable> it = list.iterator();
        while (it.hasNext()) {
            save(it.next());
        }
    }
}
