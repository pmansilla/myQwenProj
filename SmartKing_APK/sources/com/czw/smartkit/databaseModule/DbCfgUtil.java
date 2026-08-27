package com.czw.smartkit.databaseModule;

import android.content.Context;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

/* loaded from: classes.dex */
public class DbCfgUtil {
    private static DbCfgUtil dbCfgUtil = new DbCfgUtil();
    private LiteOrm liteOrm = null;

    private DbCfgUtil() {
    }

    public static DbCfgUtil getDbCfgUtil() {
        return dbCfgUtil;
    }

    public LiteOrm getLiteOrm() {
        return this.liteOrm;
    }

    public void init(Context context) {
        DataBaseConfig dataBaseConfig = new DataBaseConfig(context);
        dataBaseConfig.dbName = "appDB_release";
        dataBaseConfig.debugged = true;
        dataBaseConfig.dbVersion = 2;
        dataBaseConfig.onUpdateListener = null;
        if (this.liteOrm == null) {
            this.liteOrm = LiteOrm.newSingleInstance(dataBaseConfig);
        }
    }
}
