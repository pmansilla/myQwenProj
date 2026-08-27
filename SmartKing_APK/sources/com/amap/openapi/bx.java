package com.amap.openapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* compiled from: LocationDbOpenHelper.java */
/* loaded from: classes.dex */
class bx extends SQLiteOpenHelper {
    /* JADX INFO: Access modifiers changed from: package-private */
    public bx(Context context) {
        super(context, "OffLocation.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        bv.a(sQLiteDatabase);
        bw.a(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
