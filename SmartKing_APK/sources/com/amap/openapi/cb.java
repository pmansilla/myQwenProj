package com.amap.openapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: StatisticsDbOpenHelper.java */
/* loaded from: classes.dex */
public class cb extends SQLiteOpenHelper {
    /* JADX INFO: Access modifiers changed from: package-private */
    public cb(Context context) {
        super(context, "OffStatistics.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        bz.a(sQLiteDatabase);
        ca.a(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
