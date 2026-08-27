package com.litesuits.orm.db;

import android.content.Context;
import com.litesuits.orm.db.assit.Checker;
import com.litesuits.orm.db.assit.SQLiteHelper;

/* loaded from: classes.dex */
public class DataBaseConfig {
    public static final String DEFAULT_DB_NAME = "liteorm.db";
    public static final int DEFAULT_DB_VERSION = 1;
    public Context context;
    public String dbName;
    public int dbVersion;
    public boolean debugged;
    public SQLiteHelper.OnUpdateListener onUpdateListener;

    public DataBaseConfig(Context context) {
        this(context, DEFAULT_DB_NAME);
    }

    public DataBaseConfig(Context context, String str) {
        this(context, str, false, 1, null);
    }

    public DataBaseConfig(Context context, String str, boolean z, int i, SQLiteHelper.OnUpdateListener onUpdateListener) {
        this.debugged = false;
        this.dbName = DEFAULT_DB_NAME;
        this.dbVersion = 1;
        this.context = context.getApplicationContext();
        if (!Checker.isEmpty(str)) {
            this.dbName = str;
        }
        if (i > 1) {
            this.dbVersion = i;
        }
        this.debugged = z;
        this.onUpdateListener = onUpdateListener;
    }

    public String toString() {
        return "DataBaseConfig [mContext=" + this.context + ", mDbName=" + this.dbName + ", mDbVersion=" + this.dbVersion + ", mOnUpdateListener=" + this.onUpdateListener + "]";
    }
}
