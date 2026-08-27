package com.czw.smartkit.databaseModule.hr;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.databaseModule.DbCfgUtil;
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
public class HrServiceImpl {
    private static HrServiceImpl hrDao = new HrServiceImpl();
    private static final String tag = "db HrServiceImpl ";
    protected LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();
    private SimpleDateFormat MMddSmp = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat HHmmSmp = new SimpleDateFormat("HH:mm");
    String tabName = HrDataTable.class.getName().replaceAll("\\.", "_");

    private HrServiceImpl() {
    }

    public static HrServiceImpl getInstance() {
        return hrDao;
    }

    private void logE(String str) {
    }

    public void deleteUserAllData(String str) {
        this.liteOrm.delete(WhereBuilder.create(HrDataTable.class).where("uid=?", str));
    }

    public HrDataTable findLast(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(HrDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE).limit(AmapLoc.RESULT_TYPE_WIFI_ONLY));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (HrDataTable) query.get(0);
    }

    public List<HrDataTable> getData(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(HrDataTable.class).where("uid=? and date(dateTimeStr)==date(?) ", str, str2).appendOrderAscBy(IMAPStore.ID_DATE));
    }

    public List<HrDataTable> getDataByDesc(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(HrDataTable.class).where("uid=? and date(dateTimeStr)==date(?) ", str, str2).appendOrderDescBy(IMAPStore.ID_DATE));
    }

    public String getLastDataDate(String str) {
        HrDataTable hrDataTable;
        ArrayList query = this.liteOrm.query(QueryBuilder.create(HrDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE));
        return (query == null || query.size() <= 0 || (hrDataTable = (HrDataTable) query.get(0)) == null || TextUtils.isEmpty(hrDataTable.getDateTimeStr()) || hrDataTable.getDateTimeStr().length() <= 10) ? "2018-01-01" : hrDataTable.getDateTimeStr().substring(0, 10);
    }

    public List<HrDataTable> getNotSyncData(String str) {
        return this.liteOrm.query(QueryBuilder.create(HrDataTable.class).where("uid=? and isSync=?", str, false));
    }

    public void save(HrDataTable hrDataTable) {
        if (hrDataTable == null || TextUtils.isEmpty(hrDataTable.getUid())) {
            return;
        }
        hrDataTable.setDataId(hrDataTable.getDateTimeStr() + "_" + hrDataTable.getUid());
        StringBuilder sb = new StringBuilder();
        sb.append("debug==保存的对象==>");
        sb.append(hrDataTable.toString());
        logE(sb.toString());
        this.liteOrm.save(hrDataTable);
    }

    public void saveData(HrDataTable hrDataTable) {
        logE("debug 保存的心率对象是:" + new Gson().toJson(hrDataTable));
        if (hrDataTable == null || TextUtils.isEmpty(hrDataTable.getUid()) || TextUtils.isEmpty(hrDataTable.getDateTimeStr())) {
            return;
        }
        hrDataTable.setDataId(hrDataTable.getDateTimeStr() + "_" + hrDataTable.getUid());
        this.liteOrm.save(hrDataTable);
    }

    public void saveData(List<HrDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        Iterator<HrDataTable> it = list.iterator();
        while (it.hasNext()) {
            saveData(it.next());
        }
    }
}
