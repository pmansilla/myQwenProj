package com.czw.smartkit.databaseModule.train;

import android.database.Cursor;
import android.text.TextUtils;
import com.czw.smartkit.databaseModule.DbCfgUtil;
import com.czw.smartkit.user.UserUtil;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class TrainServiceImpl {
    private static final TrainServiceImpl ourInstance = new TrainServiceImpl();
    protected LiteOrm liteOrm = DbCfgUtil.getDbCfgUtil().getLiteOrm();
    String tabName = TrainDataTable.class.getName().replaceAll("\\.", "_");

    private TrainServiceImpl() {
    }

    public static TrainServiceImpl getInstance() {
        return ourInstance;
    }

    public void deleteUserAllData(String str) {
        this.liteOrm.delete(WhereBuilder.create(TrainDataTable.class).where("uid=?", str));
    }

    public List<TrainDataTable> getDataByDateAsc(String str, String str2) {
        return this.liteOrm.query(QueryBuilder.create(TrainDataTable.class).where("uid=? and date(startTime)=date(?)", str, str2).appendOrderAscBy("startTime"));
    }

    public String getLastDataDate(String str) {
        TrainDataTable trainDataTable;
        ArrayList query = this.liteOrm.query(QueryBuilder.create(TrainDataTable.class).where("uid=?", str).appendOrderDescBy("startTime"));
        return (query == null || query.size() <= 0 || (trainDataTable = (TrainDataTable) query.get(0)) == null || TextUtils.isEmpty(trainDataTable.getStartTime()) || trainDataTable.getStartTime().length() <= 10) ? "2018-09-09" : trainDataTable.getStartTime().substring(0, 10);
    }

    public List<TrainDataTable> getNotSyncData(String str) {
        return this.liteOrm.query(QueryBuilder.create(TrainDataTable.class).where("uid=? and isSync=?", str, false));
    }

    public void saveData(TrainDataTable trainDataTable) {
        if (trainDataTable == null || TextUtils.isEmpty(trainDataTable.getStartTime())) {
            return;
        }
        trainDataTable.setUid(UserUtil.getUid());
        trainDataTable.setDataId(trainDataTable.getStartTime() + "_" + trainDataTable.getUid());
        this.liteOrm.save(trainDataTable);
        LogUtil.e("保存的训练数据" + new Gson().toJson(trainDataTable));
    }

    public void saveData(List<TrainDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        Iterator<TrainDataTable> it = list.iterator();
        while (it.hasNext()) {
            saveData(it.next());
        }
    }

    public void saveDataForSync(List<TrainDataTable> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        for (TrainDataTable trainDataTable : list) {
            trainDataTable.setSync(true);
            saveData(trainDataTable);
        }
    }

    public TrainDataTable totalDayData(String str, String str2) {
        if (this.liteOrm == null || this.liteOrm.getReadableDatabase() == null) {
            return null;
        }
        try {
            Cursor rawQuery = this.liteOrm.getReadableDatabase().rawQuery("select  ifnull(sum(timeConsuming),0) as timeConsuming , ifnull(sum(strength),0) as strength , ifnull(sum(calorie),0) as calorie , ifnull(sum(fasterRate),0) as fasterRate  from " + this.tabName + "  where date(startTime) >=date('" + str2 + "')  and date(startTime) <=date('" + str2 + "')  and uid ='" + str + "'", null);
            if (rawQuery == null) {
                return null;
            }
            TrainDataTable trainDataTable = new TrainDataTable();
            if (rawQuery.moveToNext()) {
                trainDataTable.setTimeConsuming(Double.valueOf(rawQuery.getString(rawQuery.getColumnIndex("timeConsuming"))).intValue() + "");
                trainDataTable.setStrength(Double.valueOf(rawQuery.getString(rawQuery.getColumnIndex("strength"))).intValue() + "");
                trainDataTable.setCalorie(Double.valueOf(rawQuery.getString(rawQuery.getColumnIndex("calorie"))).intValue() + "");
                trainDataTable.setFasterRate(Double.valueOf(rawQuery.getString(rawQuery.getColumnIndex("fasterRate"))).intValue() + "");
            }
            return trainDataTable;
        } catch (Exception unused) {
            return null;
        }
    }
}
