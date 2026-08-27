package com.czw.smartkit.databaseModule.ox;

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
public class OxServiceImpl {
    private static OxServiceImpl hrDao = new OxServiceImpl();
    private static final String tag = "db HrServiceImpl ";
    protected LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();
    private SimpleDateFormat MMddSmp = new SimpleDateFormat("MM-dd");
    private SimpleDateFormat HHmmSmp = new SimpleDateFormat("HH:mm");
    String tabName = OxDataTable.class.getName().replaceAll("\\.", "_");

    private OxServiceImpl() {
    }

    public static OxServiceImpl getInstance() {
        return hrDao;
    }

    private void logE(String str) {
    }

    public void deleteUserAllData(String str) {
        this.liteOrm.delete(WhereBuilder.create(OxDataTable.class).where("uid=?", str));
    }

    public OxDataTable findLast(String str) {
        ArrayList query = this.liteOrm.query(QueryBuilder.create(OxDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE).limit(AmapLoc.RESULT_TYPE_WIFI_ONLY));
        if (query == null || query.size() < 1) {
            return null;
        }
        return (OxDataTable) query.get(0);
    }

    public List<OxDataTable> getData(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(OxDataTable.class).where("uid=? and date(dateTimeStr)==date(?) ", str, str2).appendOrderAscBy(IMAPStore.ID_DATE));
    }

    public List<OxDataTable> getDataByDesc(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(OxDataTable.class).where("uid=? and date(dateTimeStr)==date(?) ", str, str2).appendOrderDescBy(IMAPStore.ID_DATE));
    }

    public String getLastDataDate(String str) {
        OxDataTable oxDataTable;
        ArrayList query = this.liteOrm.query(QueryBuilder.create(OxDataTable.class).where("uid=?", str).appendOrderDescBy(IMAPStore.ID_DATE));
        return (query == null || query.size() <= 0 || (oxDataTable = (OxDataTable) query.get(0)) == null || TextUtils.isEmpty(oxDataTable.getDateTimeStr()) || oxDataTable.getDateTimeStr().length() <= 10) ? "2018-01-01" : oxDataTable.getDateTimeStr().substring(0, 10);
    }

    public List<OxDataTable> getNotSyncData(String str) {
        return this.liteOrm.query(QueryBuilder.create(OxDataTable.class).where("uid=? and isSync=?", str, false));
    }

    public void save(OxDataTable oxDataTable) {
        if (oxDataTable == null || TextUtils.isEmpty(oxDataTable.getUid())) {
            return;
        }
        oxDataTable.setDataId(oxDataTable.getDateTimeStr() + "_" + oxDataTable.getUid());
        StringBuilder sb = new StringBuilder();
        sb.append("debug==保存的对象==>");
        sb.append(oxDataTable.toString());
        logE(sb.toString());
        this.liteOrm.save(oxDataTable);
    }

    public void saveData(OxDataTable oxDataTable) {
        logE("debug 保存的血氧对象是:" + new Gson().toJson(oxDataTable));
        if (oxDataTable == null || TextUtils.isEmpty(oxDataTable.getUid()) || TextUtils.isEmpty(oxDataTable.getDateTimeStr())) {
            return;
        }
        oxDataTable.setDataId(oxDataTable.getDateTimeStr() + "_" + oxDataTable.getUid());
        this.liteOrm.save(oxDataTable);
    }

    public void saveData(List<OxDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        Iterator<OxDataTable> it = list.iterator();
        while (it.hasNext()) {
            saveData(it.next());
        }
    }
}
